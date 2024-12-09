package com.grayson.audiocross.data.albumlist.repository

import com.grayson.audiocross.data.albumlist.base.OrderBy
import com.grayson.audiocross.data.albumlist.base.SortMethod
import com.grayson.audiocross.data.albumlist.clients.GetWorksRequest
import com.grayson.audiocross.data.albumlist.clients.WorkClient
import com.grayson.audiocross.domain.albumlist.model.AlbumCover
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.albumlist.model.FetchAlbumListResult
import com.grayson.audiocross.domain.albumlist.repository.IAlbumListRepository
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import com.grayson.audiocross.domain.common.RequestResult

/**
 * Fetch Album List repository implementation
 */
class AlbumListRepository : IAlbumListRepository {
    override suspend fun fetchAlbumList(params: FetchAlbumListUseCase.Param): RequestResult<FetchAlbumListResult> {
        val dataResult = WorkClient().getWorks(
            GetWorksRequest(
                OrderBy.mapFromDomain(params.orderBy),
                SortMethod.mapFromDomain(params.sortMethod),
                params.page,
                params.hasSubtitle,
                params.keywords
            )
        )
        return when (dataResult) {
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
                RequestResult.Success(
                    FetchAlbumListResult(
                        albumItems = works,
                        currentPage = dataResult.data.pagination.currentPage,
                        pageSize = dataResult.data.pagination.pageSize,
                        totalCount = dataResult.data.pagination.totalCount
                    )
                )
            }

            is RequestResult.Error -> dataResult
        }
    }
}