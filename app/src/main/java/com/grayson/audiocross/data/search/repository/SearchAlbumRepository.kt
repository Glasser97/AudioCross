package com.grayson.audiocross.data.search.repository

import com.grayson.audiocross.data.albumlist.base.OrderBy
import com.grayson.audiocross.data.albumlist.base.SortMethod
import com.grayson.audiocross.data.albumlist.mapper.mapToDomain
import com.grayson.audiocross.data.search.client.SearchClient
import com.grayson.audiocross.data.search.client.SearchWorksRequest
import com.grayson.audiocross.domain.albumlist.model.FetchAlbumListResult
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.search.repository.ISearchAlbumRepository
import com.grayson.audiocross.domain.search.usecase.SearchAlbumListUseCase

class SearchAlbumRepository : ISearchAlbumRepository {
    override suspend fun searchAlbumList(params: SearchAlbumListUseCase.Param): RequestResult<FetchAlbumListResult> {
        val dataResult = SearchClient().searchWorks(
            SearchWorksRequest(
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
                    workInfo.mapToDomain()
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