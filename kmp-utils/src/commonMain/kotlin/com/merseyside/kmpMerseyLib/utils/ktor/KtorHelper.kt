package com.merseyside.kmpMerseyLib.utils.ktor

import com.merseyside.kmpMerseyLib.utils.serialization.deserialize
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.Parameters
import io.ktor.http.takeFrom
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.decodeFromString

fun HttpRequestBuilder.addHeader(key: String, value: String) {
    header(key, value)
}

fun HttpRequestBuilder.addHeaders(vararg pairs: Pair<String, String>) {
    pairs.forEach { pair -> addHeader(pair.first, pair.second) }
}

fun HttpRequestBuilder.setFormData(vararg pairs: Pair<String, String>) {
    body = FormDataContent(
        Parameters.build {
            pairs.forEach { pair -> append(pair.first, pair.second) }
        })
}

suspend inline fun <reified T: Any> KtorRouter.post(
    method: String,
    vararg queryParams: Pair<String, String>,
    deserializationStrategy: DeserializationStrategy<T>? = null,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    val uri = getRoute(method, *queryParams)

    val call = client.post<String> {
        url.takeFrom(uri)

        block()
    }

    return deserialize(call, deserializationStrategy)
}

suspend inline fun <reified T: Any> KtorRouter.get(
    method: String,
    vararg queryParams: Pair<String, String>,
    deserializationStrategy: DeserializationStrategy<T>? = null,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    val uri = getRoute(method, *queryParams)

    val call = client.get<String> {
        url.takeFrom(uri)

        block()
    }

    return deserialize(call, deserializationStrategy)
}

inline fun <reified T: Any> KtorRouter.deserialize(
    data: String,
    deserializationStrategy: DeserializationStrategy<T>? = null
): T {

    return if (deserializationStrategy != null) {
        data.deserialize(deserializationStrategy)
    } else {
        json.decodeFromString(data)
    }
}