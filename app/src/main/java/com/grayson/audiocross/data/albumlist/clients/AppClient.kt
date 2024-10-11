package com.grayson.audiocross.data.albumlist.clients

import com.grayson.audiocross.data.albumlist.clients.RequestUtil.parseBody
import com.grayson.audiocross.data.login.model.GlobalProperties
import com.grayson.audiocross.domain.common.HttpRequestApi
import io.ktor.http.*
import io.ktor.http.content.Version
import kotlinx.coroutines.runBlocking

internal class AppClient {

    fun version(token: String = GlobalProperties.Config.AccessToken!!) = runBlocking {
        val response = RequestUtil.request(
            RequestUtil.getHttpClient(authToken = token), VersionRequest
        )
        return@runBlocking response.parseBody<Version>()
    }
}

object VersionRequest : HttpRequestApi(
    httpMethod = HttpMethod.Get, urlPath = GlobalProperties.AudioCrossApi.Path.Version
)