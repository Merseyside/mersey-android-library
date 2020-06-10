package com.merseyside.kmpMerseyLib.utils.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

abstract class KtorRouter(
    val baseUrl: String,
    httpClientEngine: HttpClientEngine
) {

    @OptIn(UnstableDefault::class)
    val json = createJson()
    val client: HttpClient = createHttpClient(httpClientEngine)

    var isEncoding = false

    @OptIn(UnstableDefault::class)
    open fun createJson(): Json {
        return Json {
            isLenient = false
            ignoreUnknownKeys = true
        }
    }

    open fun createHttpClient(httpClientEngine: HttpClientEngine): HttpClient {
        return HttpClient(httpClientEngine) {
            defaultRequest {
                accept(ContentType.Application.Json)
            }
        }
    }

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