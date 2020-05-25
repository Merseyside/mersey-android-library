package com.merseyside.kmpMerseyLib.utils


expect object Logger {

    var isEnabled: Boolean
    var isDebug: Boolean

    fun log(tag: Any? = null, msg: Any? = "Empty msg")
    fun log(msg: Any? = "Empty msg")
    fun logErr(tag: Any? = null, msg: Any? = "Empty error")
    fun logErr(msg: Any? = "Empty error")
    fun logInfo(tag: Any? = null, msg: Any?)
    fun logInfo(msg: Any?)
    fun logWtf(tag: Any? = null, msg: Any? = "wtf?")
    fun logWtf(msg: Any? = "wtf?")
    fun logErr(throwable: Throwable)

    val TAG: String
}