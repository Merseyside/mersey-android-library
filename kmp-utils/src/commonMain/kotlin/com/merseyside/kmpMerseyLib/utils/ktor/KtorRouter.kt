package com.merseyside.kmpMerseyLib.utils.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.*
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import kotlinx.serialization.json.Json

abstract class KtorRouter(
    val client: HttpClient,
    val baseUrl: String
) {
    constructor(
        httpClientEngine: HttpClientEngine,
        baseUrl: String,
        defaultRequest: HttpRequestBuilder.() -> Unit = {}
    ): this(httpClientEngine.run {
        HttpClient(httpClientEngine) {
            defaultRequest {
                accept(ContentType.Application.Json)
                defaultRequest()
            }
    }}, baseUrl)

    val json = createJson()
    var isEncoding = false

    open fun createJson(): Json {
        return Json {
            isLenient = false
            ignoreUnknownKeys = true
        }
    }

    open fun handleResponse(response: Response) {}

    fun getRoute(method: String, vararg queryParams: Pair<String, String>): String {
        val uri = "$baseUrl/$method"

        return if (queryParams.isNotEmpty()) {
            val uriQueryBuilder = URIQueryBuilder(isEncoding)
            uriQueryBuilder.addParams(queryParams.toMap())

            uriQueryBuilder.addQueryToUri(uri)
        } else {
            uri
        }
    }
}