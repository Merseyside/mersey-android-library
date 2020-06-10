package com.merseyside.kmpMerseyLib.utils.ktor

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.Parameters
import io.ktor.http.takeFrom
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.parse

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

@kotlinx.serialization.ImplicitReflectionSerializer
suspend inline fun <reified T: Any> KtorRouter.post(
    method: String,
    vararg queryParams: Pair<String, String>,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    val uri = getRoute(method, *queryParams)

    val call = client.post<String> {
        url.takeFrom(uri)

        block()
    }

    return json.parse(call)
}

@kotlinx.serialization.ImplicitReflectionSerializer
suspend inline fun <reified T: Any> KtorRouter.get(

    method: String,
    vararg queryParams: Pair<String, String>,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    val uri = getRoute(method, *queryParams)

    val call = client.get<String> {
        url.takeFrom(uri)

        block()
    }

    return json.parse(call)
}