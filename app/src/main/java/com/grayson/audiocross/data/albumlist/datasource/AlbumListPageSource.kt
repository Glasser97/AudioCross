package com.grayson.audiocross.data.albumlist.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.grayson.audiocross.data.albumlist.base.OrderBy
import com.grayson.audiocross.data.albumlist.base.SortMethod
import com.grayson.audiocross.data.albumlist.clients.GetWorksRequest
import com.grayson.audiocross.data.albumlist.clients.WorkClient
import com.grayson.audiocross.domain.albumlist.model.AlbumCover
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.common.io

class AlbumListPageSource(private val initParam: FetchAlbumListUseCase.Param) :
    PagingSource<FetchAlbumListUseCase.Param, AlbumItem>() {

    override fun getRefreshKey(state: PagingState<FetchAlbumListUseCase.Param, AlbumItem>): FetchAlbumListUseCase.Param? =
        null

    override suspend fun load(params: LoadParams<FetchAlbumListUseCase.Param>): LoadResult<FetchAlbumListUseCase.Param, AlbumItem> {
        val requestParams = params.key ?: initParam
        val result = io {
            val dataResult = WorkClient().getWorks(
                GetWorksRequest(
                    OrderBy.mapFromDomain(requestParams.orderBy),
                    SortMethod.mapFromDomain(requestParams.sortMethod),
                    requestParams.page,
                    requestParams.hasSubtitle
                )
            )
            when (dataResult) {
                is RequestResult.Success -> {
                    val works = dataResult.data.works.map { workInfo ->
                        AlbumItem(
                            albumId = workInfo.id,
                            title = workInfo.title,
                            albumCode = workInfo.sourceId,
                            authorName = workInfo.vases.map { vas -> vas.name },
                            cover = AlbumCover(
                                mainCoverUrl = workInfo.mainCoverUrl,
                                mediumCoverUrl = workInfo.samCoverUrl,
                                thumbnailCoverUrl = workInfo.thumbnailCoverUrl
                            ),
                            duration = workInfo.duration
                        )
                    }
                    LoadResult.Page(
                        data = works,
                        prevKey = if (requestParams.page <= 1) null else requestParams.copy(page = requestParams.page - 1),
                        nextKey = if (dataResult.data.works.isEmpty()) null else requestParams.copy(
                            page = dataResult.data.pagination.currentPage + 1
                        )

                    )
                }

                is RequestResult.Error -> LoadResult.Error(dataResult.exception)
            }
        }
        return result
    }
}