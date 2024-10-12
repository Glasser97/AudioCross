package com.grayson.audiocross.data.albumlist.clients

import android.util.Log
import com.grayson.audiocross.data.login.model.GlobalProperties
import com.grayson.audiocross.domain.common.HttpRequestApi
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.exceptions.CommonError
import com.grayson.audiocross.domain.exceptions.RequestNotOkException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.gson.gson
import io.ktor.util.cio.readChannel
import kotlinx.coroutines.runBlocking
import java.net.Proxy

object RequestUtil {

    fun getHttpClient(
        requestProxy: Proxy = GlobalProperties.Config.GlobalProxy,
        authToken: String? = null
    ): HttpClient {
        return HttpClient(OkHttp) {
            install(Logging) {
                logger = GlobalProperties.Config.GlobalLogger
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                gson(contentType = ContentType.Application.Json)
            }
            engine {
                if (requestProxy != Proxy.NO_PROXY) {
                    proxy = requestProxy
                }
            }
            authToken?.let { token ->
                install(Auth) {
                    bearer {
                        loadTokens {
                            BearerTokens(token, token)
                        }
                    }
                }
            }
        }
    }

    suspend fun request(
        httpClient: HttpClient,
        httpRequestApi: HttpRequestApi
    ): HttpResponse? {
        return try {
            httpClient.request {
                method = httpRequestApi.httpMethod
                url {
                    protocol = URLProtocol.HTTPS
                    host = httpRequestApi.urlHost
                    path(httpRequestApi.urlPath)
                    httpRequestApi.urlParams?.let { urlParams ->
                        urlParams.forEach { (key, value) ->
                            parameters.append(key, value)
                        }
                    }
                }
                headers {
                    append(HttpHeaders.Accept, httpRequestApi.acceptContentType)
                    append(HttpHeaders.UserAgent, GlobalProperties.Config.UserAgent)
                }
                httpRequestApi.binaryFile?.let { file ->
                    setBody(file.readChannel())
                }
                httpRequestApi.jsonBody?.let { body ->
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
            }
        } catch (e: Exception) {
            Log.e("RequestUtil", "request: ${httpRequestApi.urlPath} failed", e)
            null
        }
    }

    inline fun <reified T> HttpResponse?.parseBody(): RequestResult<T> = runBlocking {
        this@parseBody
            ?: return@runBlocking RequestResult.Error(RequestNotOkException(CommonError("Response is null")))
        return@runBlocking try {
            RequestResult.Success(body<T>())
        } catch (e: JsonConvertException) {
            RequestResult.Error(RequestNotOkException(body()))
        }
    }
}