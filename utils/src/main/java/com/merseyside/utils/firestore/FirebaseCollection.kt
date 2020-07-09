package com.merseyside.utils.firestore

import com.google.firebase.firestore.CollectionReference
import com.merseyside.utils.ext.isNotNullAndEmpty
import com.merseyside.utils.ext.log
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseCollection<T: IFirestoreInstance> internal constructor(
    val collection: CollectionReference
): BaseFirebaseInstance<T>() {

    fun getDocument(docPath: String): FirebaseDocument<T> {
        return FirebaseDocument(collection.document(docPath))
    }

    fun getReference(): CollectionReference {
        return collection
    }

    fun addAsync(
        data: T,
        docPath: String?,
        onSuccess: (String) -> Unit,
        onError: (String, Throwable) -> Unit
    ) {

        val document = FirebaseDocument<T>(
            if (docPath != null) collection.document(docPath)
            else collection.document()
        ).apply { setFieldMerger(merger) }

        document.setAsync(data, onSuccess, onError)
    }

    suspend fun add(
        data: T,
        docPath: String?
    ) = suspendCancellableCoroutine<T> { continuation ->
        addAsync(data,
            onSuccess = {
                continuation.resume(data)
            }, onError = { id, throwable ->
                continuation.cancel(throwable)
            }, docPath = docPath
        )
    }

    suspend fun add(
        dataList: List<T>
    ): List<T> {
        val job = async {
            dataList.map { data ->

                val docPath = data.documentPath

                add(data, docPath)
            }
        }

        return job.await()
    }

    suspend fun add(
        vararg data: T
    ): List<T> {
        return add(data.toList())
    }

    fun update(
        data: T,
        docPath: String? = null,
        onSuccess: (String) -> Unit,
        onError: (String, Throwable) -> Unit
    ) {

        val document = FirebaseDocument<T>(
            if (docPath != null) collection.document(docPath)
            else collection.document()
        ).apply { setFieldMerger(merger) }

        document.setAsync(data, onSuccess, onError)
    }

    fun update(
        dataList: List<T>,
        onSuccess: (String) -> Unit,
        onError: (String, Throwable) -> Unit
    ) {
        dataList.forEach { data ->

            val docPath = data.documentPath

            update(data, docPath, onSuccess, onError)
        }
    }

    fun update(
        vararg data: T,
        onSuccess: (String) -> Unit,
        onError: (String, Throwable) -> Unit
    ) {
        update(data.toList(), onSuccess, onError)
    }

    inline fun <reified T: IFirestoreInstance> getAsync(
        documentPath: String? = null,
        crossinline onSuccess: (List<T>) -> Unit,
        crossinline onError: (Throwable) -> Unit
    ) {

        if (documentPath.isNotNullAndEmpty()) {
            collection.document(documentPath!!).get()
                .addOnSuccessListener { response ->

                    if (isLog) {
                        response?.data?.log(this, "response = ")
                    }

                    val obj = response.toObject(T::class.javaObjectType)?.apply { this.documentPath = response.id }

                    if (obj != null) {
                        onSuccess.invoke(listOf(obj))
                    } else {
                        onError.invoke(KotlinNullPointerException())
                    }

                }.addOnFailureListener { throwable ->
                    onError.invoke(throwable)
                }
        } else {

            collection.get()
                .addOnSuccessListener { response ->
                    onSuccess.invoke(response.documents.mapNotNull {
                        it.toObject(T::class.javaObjectType)?.apply { this.documentPath = it.id }
                    })
                }.addOnFailureListener { throwable ->
                    onError.invoke(throwable)
                }
        }
    }

    suspend inline fun <reified T: IFirestoreInstance> get()
            = suspendCancellableCoroutine<List<T>?> { continuation ->
        getAsync<T>(
            documentPath = null,
            onSuccess = {
                continuation.resume(it)
            }, onError = { throwable ->
                continuation.cancel(throwable)
            }
        )
    }

    suspend inline fun <reified T: IFirestoreInstance> get(
        documentPath: String
    ) = suspendCancellableCoroutine<T?> { continuation ->
        getAsync<T>(
            documentPath = documentPath,
            onSuccess = {
                continuation.resume(it.firstOrNull())
            }, onError = { throwable ->
                continuation.cancel(throwable)
            }
        )
    }

    suspend inline fun <reified T: IFirestoreInstance> get(
        documentPathList: List<String>
    ): List<T> {

        val job = async {
            documentPathList.mapNotNull { get<T>(it) }
        }

        return job.await()
    }

}