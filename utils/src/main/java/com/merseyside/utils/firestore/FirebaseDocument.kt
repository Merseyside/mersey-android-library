package com.merseyside.utils.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseDocument<T: IFirestoreInstance> internal constructor(
    private val document: DocumentReference
): BaseFirebaseInstance<T>() {

    fun getReference(): DocumentReference {
        return document
    }

    suspend fun <M: IFirestoreInstance> add(
        collectionPath: String,
        dataList: List<M>
    ): List<M> {
        val collection = FirebaseCollection<M>(document.collection(collectionPath))

        return collection.add(dataList)
    }

    suspend fun <M: IFirestoreInstance> add(
        collectionPath: String,
        vararg data: M
    ): List<M> = add(collectionPath, data.toList())

    fun setAsync(
        data: T,
        onSuccess: (id: String) -> Unit,
        onError: (String, Throwable) -> Unit
    ) {
        document.set(data,
                if (merger == null) SetOptions.merge()
                else SetOptions.mergeFields(merger!!.getMergeFields())
        ).addOnSuccessListener {
            data.documentPath = document.id
            onSuccess.invoke(document.id)
        }
            .addOnFailureListener { e -> onError.invoke(document.id, e) }
    }

    suspend fun set(data: T): T = suspendCancellableCoroutine { continuation ->
        setAsync(
            data,
            onSuccess = {
                continuation.resume(data)
            }, onError = { id, throwable ->
                continuation.cancel(throwable)
            })
    }

}