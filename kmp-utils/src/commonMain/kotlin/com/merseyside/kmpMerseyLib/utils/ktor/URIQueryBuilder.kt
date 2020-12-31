package com.merseyside.kmpMerseyLib.utils.ktor

import com.merseyside.kmpMerseyLib.utils.ext.toUtf8

class URIQueryBuilder(private val isEncoding: Boolean) {

    private var params: MutableMap<String, String> = LinkedHashMap()

    val string: String
        get() {
            val builder = StringBuilder()
            for ((key, value) in params) {
                if (builder.isNotEmpty()) builder.append('&')

                builder.append(toFormat(key))
                builder.append('=')
                builder.append(toFormat(value))

            }
            return builder.toString()
        }

    fun addParam(key: String, value: String) {
        params[key] = value
    }

    fun addParams(params: Map<String, String>) {
        this.params.putAll(params)
    }

    fun clear() {
        params.clear()
    }

    fun getParams(): Map<String, String> {
        return params
    }

    fun addQueryToUri(uri: String): String {
        return "$uri?$string"
    }

    fun toFormat(str: String): String {
        return if (isEncoding) str.toUtf8() else str
    }
}