package com.merseyside.kmpMerseyLib.utils.ktor

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.util.*

class TokenFeature private constructor(
    private val tokenHeaderName: String,
    private val tokenProvider: TokenProvider
) {

    class Config {
        var tokenHeaderName: String? = null
        var tokenProvider: TokenProvider? = null
        fun build() = TokenFeature(
            tokenHeaderName ?: throw IllegalArgumentException("HeaderName should be contain"),
            tokenProvider ?: throw IllegalArgumentException("TokenProvider should be contain")
        )
    }

    companion object Feature : HttpClientFeature<Config, TokenFeature> {
        override val key = AttributeKey<TokenFeature>("TokenFeature")

        override fun prepare(block: Config.() -> Unit) = Config().apply(block).build()

        override fun install(feature: TokenFeature, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                feature.tokenProvider.getToken()?.apply {
                    context.headers.remove(feature.tokenHeaderName)
                    context.header(feature.tokenHeaderName, this)
                }
            }
        }
    }

    interface TokenProvider {
        fun getToken(): String?
    }
}