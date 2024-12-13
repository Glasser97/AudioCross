package com.grayson.audiocross.data.search.client

import com.grayson.audiocross.data.albumlist.base.OrderBy
import com.grayson.audiocross.data.albumlist.base.SortMethod
import com.grayson.audiocross.data.albumlist.clients.RequestUtil
import com.grayson.audiocross.data.albumlist.clients.RequestUtil.parseBody
import com.grayson.audiocross.data.albumlist.model.work.PaginationWorks
import com.grayson.audiocross.data.login.model.GlobalProperties
import com.grayson.audiocross.domain.common.HttpRequestApi
import io.ktor.http.HttpMethod
import kotlinx.coroutines.runBlocking

internal class SearchClient {
    fun searchWorks(
        searchWorksRequest: SearchWorksRequest
    ) = runBlocking {
        val response = RequestUtil.request(
            RequestUtil.getHttpClient(authToken = GlobalProperties.Config.AccessToken),
            searchWorksRequest
        )
        return@runBlocking response.parseBody<PaginationWorks>()
    }
}

class SearchWorksRequest(
    val orderBy: OrderBy,
    val sortMethod: SortMethod,
    val page: Int,
    val hasSubtitle: Boolean,
    keywords: String? = null
) : HttpRequestApi(httpMethod = HttpMethod.Get,
    urlPath = GlobalProperties.AudioCrossApi.Path.SearchWorks + "/$keywords",
    urlParams = HashMap<String, String>().also { map ->
        map["order"] = orderBy.key
        map["sort"] = sortMethod.key
        map["page"] = "$page"
        if (orderBy == OrderBy.NO_NSFW) {
            map["label"] = "works.sfwOnly"
        }
        map["includeTranslationWorks"] = "true"
        map["subtitle"] = if (hasSubtitle) "1" else "0"
    })