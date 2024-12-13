package com.grayson.audiocross.domain.search.repository

import com.grayson.audiocross.domain.albumlist.model.FetchAlbumListResult
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.search.usecase.SearchAlbumListUseCase

interface ISearchAlbumRepository {
    suspend fun searchAlbumList(params: SearchAlbumListUseCase.Param): RequestResult<FetchAlbumListResult>
}