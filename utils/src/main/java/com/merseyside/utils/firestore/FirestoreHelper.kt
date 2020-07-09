package com.merseyside.utils.firestore

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreHelper(private val db: FirebaseFirestore) {

    fun <T: IFirestoreInstance> getCollection(collectionPath: String): FirebaseCollection<T> {
        return FirebaseCollection(db.collection(collectionPath))
    }
}