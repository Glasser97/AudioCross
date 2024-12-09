package com.grayson.audiocross.data.albumlist.clients

import com.grayson.audiocross.data.albuminfo.model.AlbumTrackData
import com.grayson.audiocross.data.albumlist.base.OrderBy
import com.grayson.audiocross.data.albumlist.base.SortMethod
import com.grayson.audiocross.data.albumlist.clients.RequestUtil.parseBody
import com.grayson.audiocross.data.albumlist.model.work.PaginationWorks
import com.grayson.audiocross.data.albumlist.model.work.WorkInfo
import com.grayson.audiocross.data.login.model.GlobalProperties
import com.grayson.audiocross.domain.common.HttpRequestApi
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

internal class WorkClient {
    fun getWorks(
        getWorksRequest: GetWorksRequest
    ) = runBlocking {
        val response = RequestUtil.request(
            RequestUtil.getHttpClient(authToken = GlobalProperties.Config.AccessToken),
            getWorksRequest
        )
        return@runBlocking response.parseBody<PaginationWorks>()
    }

    fun getWorkInfo(
        getWorkInfoRequest: GetWorkInfoRequest
    ) = runBlocking {
        val response = RequestUtil.request(
            RequestUtil.getHttpClient(authToken = GlobalProperties.Config.AccessToken),
            getWorkInfoRequest
        )
        return@runBlocking response.parseBody<WorkInfo>()
    }

    fun getWorkTracks(
        getWorkTracksRequest: GetWorkTracksRequest
    ) = runBlocking {
        val response = RequestUtil.request(
            RequestUtil.getHttpClient(authToken = GlobalProperties.Config.AccessToken),
            getWorkTracksRequest
        )
        return@runBlocking response.parseBody<List<AlbumTrackData>>()
    }
}

class GetWorkInfoRequest(
    val id: Long
) : HttpRequestApi(
    httpMethod = HttpMethod.Get,
    urlPath = "${GlobalProperties.AudioCrossApi.Path.WorkInfo}/${id}",
)

class GetWorkTracksRequest(
    val id: Long
) : HttpRequestApi(
    httpMethod = HttpMethod.Get,
    urlPath = "${GlobalProperties.AudioCrossApi.Path.WorkTracks}/${id}",
)

class GetWorksRequest(
    val orderBy: OrderBy,
    val sortMethod: SortMethod,
    val page: Int,
    val hasSubtitle: Boolean,
    val keywords: String? = null
) : HttpRequestApi(httpMethod = HttpMethod.Get,
    urlPath = GlobalProperties.AudioCrossApi.Path.AllWorks,
    urlParams = HashMap<String, String>().also { map ->
        map["order"] = OrderBy.NO_NSFW.key
        map["sort"] = SortMethod.ASCENDING.key
        map["page"] = "$page"
        map["label"] = "works.sfwOnly"
        if (!keywords.isNullOrBlank()) {
            map["keyword"] = keywords
        }
        if (orderBy == OrderBy.RANDOM) {
            map["seed"] = "${GlobalProperties.Config.Seed}"
        }
        map["subtitle"] = if (hasSubtitle) "1" else "0"
    })


