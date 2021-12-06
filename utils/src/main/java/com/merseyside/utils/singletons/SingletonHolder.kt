package com.merseyside.merseyLib.kotlin.singletons

/**
 * Example:
 * class Manager private constructor(context: Context) {
 *   init {
 *     // Init using context argument
 *   }
 *   companion object : SingletonHolder<Manager, Context>(::Manager)
 */

open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun init(arg: A) {
        val i = instance
        if (i != null) {
            throw Exception("Already initialized!")
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
            }
        }
    }

    fun getInstance(): T {
        if (instance != null) {
            return instance!!
        } else throw Exception("Not initialized!")
    }
}