package com.merseyside.merseyLib.kotlin.singletons

/**
 * Example:
 * class Manager private constructor(context: Context) {
 *   init {
 *     // Init using context argument
 *   }
 *   companion object : SingletonHolder<Manager, Context>(::Manager)
 */

open class ThirdSingletonHolder<out T: Any, in A, in B, in C>(creator: (A, B, C) -> T) {
    private var creator: ((A, B, C) -> T)? = creator
    @Volatile private var instance: T? = null

    fun init(arg1: A, arg2: B, arg3: C): T {
        val i = instance
        if (i != null) {
            throw Exception("Already initialized!")
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg1, arg2, arg3)
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