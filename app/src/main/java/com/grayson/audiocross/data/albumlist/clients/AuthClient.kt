package com.grayson.audiocross.data.albumlist.clients

import com.grayson.audiocross.data.albumlist.clients.RequestUtil.parseBody
import com.grayson.audiocross.data.albumlist.model.auth.Login
import com.grayson.audiocross.data.albumlist.model.auth.Register
import com.grayson.audiocross.domain.common.HttpRequestApi
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

internal class AuthClient {

    fun login(name: String, password: String) = runBlocking {
        val response = RequestUtil.request(
            RequestUtil.getHttpClient(),
            LoginRequest(name, password)
        )
        return@runBlocking response.parseBody<Login>()
    }

    fun register(name: String, password: String) = runBlocking {
        val response = RequestUtil.request(
            RequestUtil.getHttpClient(),
            RegisterRequest(name, password)
        )
        return@runBlocking response.parseBody<Register>()
    }

    fun authMe(token: String = GlobalProperties.Config.AccessToken!!) = runBlocking {
        val response = RequestUtil.request(
            RequestUtil.getHttpClient(authToken = token),
            AuthMe
        )
        return@runBlocking response.parseBody<AuthMe>()
    }
}

class LoginRequest(name: String, password: String) : HttpRequestApi(
    httpMethod = HttpMethod.Post,
    urlPath = GlobalProperties.AudioCrossApi.Path.AuthMe,
    urlParams = HashMap<String, String>().also { map ->
        map["name"] = name
        map["password"] = password
    }
)

class RegisterRequest(name: String, password: String) : HttpRequestApi(
    httpMethod = HttpMethod.Post,
    urlPath = GlobalProperties.AudioCrossApi.Path.Register,
    jsonBody = HashMap<String, String>().also { map ->
        map["name"] = name
        map["password"] = password
    }
)

object AuthMe : HttpRequestApi(
    httpMethod = HttpMethod.Get,
    urlPath = GlobalProperties.AudioCrossApi.Path.AuthMe
)