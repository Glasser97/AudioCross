package com.grayson.audiocross.domain.common

import com.grayson.audiocross.data.albumlist.clients.GlobalProperties
import io.ktor.http.HttpMethod
import java.io.File

/**
 * Base class for all API requests.
 */
open class HttpRequestApi(
    val httpMethod: HttpMethod,
    val urlHost: String = GlobalProperties.AudioCrossApi.host,
    val urlPath: String = "",
    val acceptContentType: String = "text/json",
    var urlParams: Map<String, String>? = null,
    var binaryFile: File? = null,
    var jsonBody: Map<String, Any?>? = null)