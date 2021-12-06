package com.merseyside.merseyLib.kotlin.singletons


/**
 * Example:
 * class Manager private constructor(context: Context) {
 *   init {
 *     // Init using context argument
 *   }
 *   companion object : SingletonHolder<Manager, Context>(::Manager)
 */

open class DoubleSingletonHolder<out T: Any, in A, in B>(creator: (A, B) -> T) {
    private var creator: ((A, B) -> T)? = creator
    @Volatile private var instance: T? = null

    fun init(arg1: A, arg2: B): T {
        val i = instance
        if (i != null) {
            throw Exception("Already initialized!")
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg1, arg2)
                instance = created
                creator = null
                created
            }
        }
    }

    fun getInstance(): T {
        if (instance != null) {
            return instance!!
        } else throw Exception("Not initialized!")
    }
}