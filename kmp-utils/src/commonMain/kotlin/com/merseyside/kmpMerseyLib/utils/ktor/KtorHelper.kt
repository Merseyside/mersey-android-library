package com.merseyside.kmpMerseyLib.utils.ktor

import com.merseyside.kmpMerseyLib.utils.serialization.deserialize
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.decodeFromString

typealias Response = Any

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
    return request(
        HttpMethod.Post,
        method, *queryParams,
        deserializationStrategy = deserializationStrategy,
        block = block
    )
}

suspend inline fun <reified T: Any> KtorRouter.get(
    method: String,
    vararg queryParams: Pair<String, String>,
    deserializationStrategy: DeserializationStrategy<T>? = null,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return request(
        HttpMethod.Get,
        method, *queryParams,
        deserializationStrategy = deserializationStrategy,
        block = block
    )
}

suspend inline fun <reified T: Any> KtorRouter.delete(
    method: String,
    vararg queryParams: Pair<String, String>,
    deserializationStrategy: DeserializationStrategy<T>? = null,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return request(
        HttpMethod.Delete,
        method, *queryParams,
        deserializationStrategy = deserializationStrategy,
        block = block
    )
}

suspend inline fun <reified T: Any> KtorRouter.put(
    method: String,
    vararg queryParams: Pair<String, String>,
    deserializationStrategy: DeserializationStrategy<T>? = null,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return request(
        HttpMethod.Put,
        method, *queryParams,
        deserializationStrategy = deserializationStrategy,
        block = block
    )
}

suspend inline fun <reified T: Any> KtorRouter.request(
    httpMethod: HttpMethod,
    method: String,
    vararg queryParams: Pair<String, String>,
    deserializationStrategy: DeserializationStrategy<T>? = null,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    val uri = getRoute(method, *queryParams)

    val call = client.request<String> {
        this.method = httpMethod
        url.takeFrom(uri)

        block()
    }

    return deserialize(call, deserializationStrategy).also { handleResponse(it) }
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