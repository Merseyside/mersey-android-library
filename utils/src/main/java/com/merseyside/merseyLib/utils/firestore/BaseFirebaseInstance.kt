package com.merseyside.merseyLib.utils.firestore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BaseFirebaseInstance<T>: CoroutineScope {

    var isLog = false

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    interface FieldMerger {
        fun getMergeFields(): List<String>
    }

    protected var merger: FieldMerger? = null

    fun setFieldMerger(fieldMerger: FieldMerger?) {
        this.merger = fieldMerger
    }
}