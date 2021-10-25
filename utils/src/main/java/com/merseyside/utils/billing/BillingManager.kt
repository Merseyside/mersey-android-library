package com.merseyside.utils.billing

import android.app.Activity
import android.content.Context
import androidx.annotation.RawRes
import com.android.billingclient.api.*
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.merseyside.utils.Logger
import com.merseyside.utils.billing.Security.verifyPurchase
import com.merseyside.utils.ext.getApplicationName
import com.merseyside.utils.ext.isNotNullAndEmpty
import com.merseyside.utils.ext.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BillingManager(
    private val context: Context,
    private val base64Key: String,
    @RawRes private val credentialsId: Int? = null
) : PurchasesUpdatedListener, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    interface BillingConnectionListener {
        fun onConnected()
        fun onError(code: Int)
        fun onDisconnected()
    }

    interface OnPurchaseListener {
        fun onPurchase(purchase: Purchase)
        fun onError(result: BillingResult)
    }

    private var onPurchaseListener: OnPurchaseListener? = null

    fun setOnPurchaseListener(listener: OnPurchaseListener) {
        this.onPurchaseListener = listener
    }

    private var billingClient: BillingClient? = null

    private suspend fun startConnection(): BillingClient? {
        return if (billingClient == null) {
            suspendCancellableCoroutine { cont ->
                val client = BillingClient
                    .newBuilder(context)
                    .enablePendingPurchases()
                    .setListener(this@BillingManager)
                    .build()

                client.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (cont.isActive) {
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                billingClient = client
                                cont.resume(billingClient)
                            } else {
                                cont.resume(null)
                            }
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        billingClient = null
                    }
                })
            }
        } else {
            billingClient
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            if (purchases != null) {
                purchases.forEach { purchase ->
                    if (verifyValidSignature(
                            purchase.originalJson,
                            purchase.signature
                        )
                    ) {
                        onPurchaseListener?.onPurchase(purchase)
                    } else {
                        onPurchaseListener?.onError(result)
                    }
                }
            } else {
                onPurchaseListener?.onError(result)
            }
        } else {
            onPurchaseListener?.onError(result)
        }
    }

    private fun verifyValidSignature(
        signedData: String,
        signature: String
    ): Boolean {
        return try {
            verifyPurchase(
                base64Key,
                signedData,
                signature
            )
        } catch (e: IOException) {
            Logger.logErr(this, "Got an exception trying to validate a purchase: ${e.message}")
            false
        }
    }

    /**
     * Get subscriptions from Google Play
     *
     * @return null if something went wrong, sku details list otherwise
     */
    suspend fun getSkuDetails(skuList: List<String>? = null): List<SkuDetails>? {

        if (skuList.isNotNullAndEmpty()) {
            Logger.log(this, this)
            billingClient = startConnection()

            return billingClient?.let { client ->
                val skuDetailsParams = SkuDetailsParams.newBuilder().setSkusList(skuList)
                    .setType(BillingClient.SkuType.SUBS).build()

                return suspendCoroutine { cont ->
                    client.querySkuDetailsAsync(
                        skuDetailsParams
                    ) { result, responseSkuList ->
                        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                            cont.resume(responseSkuList)
                        } else {
                            cont.resume(null)
                        }
                    }
                }
            }
        } else throw IllegalArgumentException("Sku list can not be empty")
    }

    suspend fun startSubscription(activity: Activity, sku: String): BillingResult {
        val details = getSkuDetails(listOf(sku))
        return details?.let {
            return startSubscription(activity, it.first())
        } ?: throw IllegalArgumentException("Sku $sku not found!")
    }

    fun startSubscription(activity: Activity, skuDetails: SkuDetails): BillingResult {
        Logger.log(this, this)
        if (billingClient != null) {
            val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build()

            return billingClient!!.launchBillingFlow(activity, flowParams)
        } else {
            throw IllegalStateException("BillingClient is null")
        }
    }

    suspend fun queryAllSubscriptionsAsync(
        vararg skus: String,
        isKeepActiveOnError: Boolean = false
    ): List<Subscription>? {

        if (credentialsId != null) {

            val billingClient = startConnection()

            if (billingClient != null) {
                val historyList = suspendCoroutine<List<PurchaseHistoryRecord>?> { cont ->
                    billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS,
                        PurchaseHistoryResponseListener { result, historyList ->
                            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                                cont.resume(historyList)
                            } else {
                                cont.resume(null)
                            }
                        })
                }.log()

                return historyList
                    ?.filter { it.skus.find { historySkus -> skus.contains(historySkus) } != null }
                    ?.flatMap { purchase ->
                        purchase.skus.mapNotNull {
                            if (skus.contains(it)) {
                                getSubscriptionState(
                                    it,
                                    purchase.purchaseToken,
                                    isKeepActiveOnError
                                )
                            } else null
                        }
                    }
            } else {
                return null
            }
        } else throw IllegalArgumentException("Please, set credentials id by constructor")
    }

    suspend fun queryActiveSubscriptionsAsync(
        vararg skus: String,
        isKeepActiveOnError: Boolean = false
    ): List<Subscription>? {
        return queryAllSubscriptionsAsync(
            *skus,
            isKeepActiveOnError = isKeepActiveOnError
        )?.filterIsInstance<Subscription.ActiveSubscription>()
    }

    private suspend fun getSubscriptionState(
        sku: String,
        token: String,
        isKeepActiveOnError: Boolean
    ): Subscription {

        val httpTransport = NetHttpTransport()
        val jacksonJsonFactory = GsonFactory.getDefaultInstance()

        val packageName = context.packageName

        val publisher = AndroidPublisher.Builder(
            httpTransport,
            jacksonJsonFactory,
            HttpCredentialsAdapter(getGoogleCredentials())
        )
            .setApplicationName(context.getApplicationName())
            .build()

        val request = publisher.Purchases().subscriptions().get(packageName, sku, token)

        var isKeepActiveOnErrorMut = isKeepActiveOnError

        return withContext(Dispatchers.IO) {
            Subscription.Builder(
                try {
                    request.execute()
                } catch (e: GoogleJsonResponseException) {
                    isKeepActiveOnErrorMut = false
                    null
                } catch (e: HttpResponseException) { null },
                isKeepActiveOnError = isKeepActiveOnErrorMut
            ).setSku(sku).build()
        }
    }

    suspend fun startTestPurchase(activity: Activity) {
        val skuList = getSkuDetails(listOf(TEST_SUBSCRIPTION_ID)).log()

//        if (skuList.isNotNullAndEmpty()) {
//            startSubscription(activity, skuList!!.first()) {
//                Logger.log(this, "response code = $it")
//            }
//        } else {
//            throw IllegalArgumentException("No test products")
//        }
    }

    private fun getGoogleCredentials(): GoogleCredentials {
        credentialsId?.let {
            context.resources.openRawResource(it).use { `is` ->
                return GoogleCredentials.fromStream(`is`)
                    .createScoped(PUBLISHER_SCOPE)
            }
        } ?: throw IllegalArgumentException("Credential ids not set!")
    }

    companion object {
        private const val TEST_SUBSCRIPTION_ID = "android.test.purchased"
        private const val PUBLISHER_SCOPE = "https://www.googleapis.com/auth/androidpublisher"
    }
}