package com.grayson.audiocross.data.albumlist.clients

import com.grayson.audiocross.data.albumlist.base.OrderBy
import com.grayson.audiocross.data.albumlist.base.SortMethod
import com.grayson.audiocross.data.albumlist.clients.RequestUtil.parseBody
import com.grayson.audiocross.data.albumlist.model.work.PaginationWorks
import com.grayson.audiocross.domain.common.HttpRequestApi
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

internal class WorkClient {
    fun getWorks(
        getWorksRequest: GetWorksRequest,
        seed: Int = GlobalProperties.Config.Seed
    ) = runBlocking {
        val response = RequestUtil.request(
            RequestUtil.getHttpClient(authToken = GlobalProperties.Config.AccessToken),
            getWorksRequest
        )
        return@runBlocking response.parseBody<PaginationWorks>()
    }
}

class GetWorksRequest(
    val orderBy: OrderBy,
    val sortMethod: SortMethod,
    val page: Int,
    val hasSubtitle: Boolean
) : HttpRequestApi(
    httpMethod = HttpMethod.Get,
    urlPath = GlobalProperties.AudioCrossApi.Path.AllWorks,
    urlParams = HashMap<String, String>().also { map ->
        map["order"] = orderBy.key
        map["sort"] = sortMethod.key
        map["page"] = "$page"
        if (orderBy == OrderBy.RANDOM) {
            map["seed"] = "${GlobalProperties.Config.Seed}"
        }
        map["subtitle"] = if (hasSubtitle) "1" else "0"
    }
)


