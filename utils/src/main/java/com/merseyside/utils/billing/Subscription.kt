package com.merseyside.utils.billing

import com.google.api.services.androidpublisher.model.SubscriptionPurchase
import com.merseyside.merseyLib.time.Time
import com.merseyside.merseyLib.time.units.Millis
import kotlinx.serialization.Serializable

@Serializable
sealed class Subscription {
    abstract val orderId: String
    abstract val purchaseToken: String
    abstract val isExists: Boolean
    abstract val sku: String
    internal abstract val startTimeMillis: Long
    internal abstract val expiryTimeMillis: Long
    abstract val autoRenewing: Boolean
    abstract val priceCode: String
    internal abstract val priceAmountMicros: Long
    abstract val countryCode: String

    enum class PaymentState(private val code: Int) {
        PENDING(0), ACTIVE(1), TRIAL(2), ANOTHER(3);

        companion object {
            fun valueOf(code: Int): PaymentState? {
                values().forEach {
                    if (it.code == code) return it
                }

                return null
            }
        }
    }

    fun getStartTimeMillis(): Millis {
        return Millis(startTimeMillis)
    }

    fun getExpiryTimeMillis(): Millis {
        return Millis(expiryTimeMillis)
    }

    fun getPrice(): Float {
        return priceAmountMicros / 1_000_000F
    }

    @Serializable
    data class CanceledSubscription(
        override val orderId: String = "",
        override val purchaseToken: String = "",
        override val isExists: Boolean,
        override val sku: String,
        override val startTimeMillis: Long = 0,
        override val expiryTimeMillis: Long = 0,
        override val autoRenewing: Boolean = false,
        override val priceCode: String = "",
        override val priceAmountMicros: Long = 0,
        override val countryCode: String = "",

        internal val cancelReason: Int = 0
    ) : Subscription() {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }

        /**
         * @param cancelReason
         * 0 - User canceled the subscription
         * 1 - Subscription was canceled by the system, for example because of a billing problem
         * 2 - Subscription was replaced with a new subscription
         * 3 - Subscription was canceled by the developer
         */
        fun getCancelReason(): Int {
            return cancelReason
        }

    }

    @Serializable
    data class ActiveSubscription(
        override val orderId: String = "",
        override val purchaseToken: String = "",
        override val isExists: Boolean = true,
        override val sku: String,
        override val startTimeMillis: Long = 0,
        override val expiryTimeMillis: Long = 0,
        override val autoRenewing: Boolean = false,
        override val priceCode: String = "",
        override val priceAmountMicros: Long = 0,
        override val countryCode: String = "",

        private val paymentState: Int = 0

    ) : Subscription() {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }

        fun getPaymentState(): PaymentState {
            return PaymentState.valueOf(paymentState) ?: throw IllegalArgumentException("Unknown payment state")
        }

    }

    internal class Builder(
        private val subscriptionPurchase: SubscriptionPurchase?,
        private val isKeepActiveOnError: Boolean = false
    ) {

        private var sku: String? = null

        fun setSku(sku: String): Builder {
            this.sku = sku

            return this
        }

        fun build(): Subscription {
            if (sku != null) {

                if (subscriptionPurchase != null) {

                    return subscriptionPurchase.let {


                        if (it.expiryTimeMillis < Time.nowGMT.millis) {
                            CanceledSubscription(
                                orderId = it.orderId,
                                purchaseToken = it.linkedPurchaseToken,
                                isExists = true,
                                sku = sku!!,
                                startTimeMillis = it.startTimeMillis,
                                expiryTimeMillis = it.expiryTimeMillis,
                                autoRenewing = it.autoRenewing,
                                priceCode = it.priceCurrencyCode,
                                priceAmountMicros = it.priceAmountMicros,
                                countryCode = it.countryCode,
                                cancelReason = it.cancelReason ?: 0
                            )
                        } else {
                            ActiveSubscription(
                                orderId = it.orderId,
                                purchaseToken = it.linkedPurchaseToken,
                                sku = sku!!,
                                startTimeMillis = it.startTimeMillis,
                                expiryTimeMillis = it.expiryTimeMillis,
                                autoRenewing = it.autoRenewing,
                                priceCode = it.priceCurrencyCode,
                                priceAmountMicros = it.priceAmountMicros,
                                countryCode = it.countryCode,
                                paymentState = it.paymentState
                            )
                        }
                    }
                } else {

                    return if (isKeepActiveOnError) {
                        ActiveSubscription(
                            sku = sku!!,
                            isExists = false
                        )
                    } else {
                        CanceledSubscription(
                            sku = sku!!,
                            isExists = false
                        )
                    }
                }
            } else throw IllegalArgumentException("Sku is null")
        }
    }
}