package com.grayson.audiocross.domain.albumlist.repository

import com.grayson.audiocross.domain.albumlist.model.FetchAlbumListResult
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import com.grayson.audiocross.domain.common.RequestResult

/**
 * Fetch Album List repository interface
 */
interface IAlbumListRepository {
    suspend fun fetchAlbumList(params: FetchAlbumListUseCase.Param): RequestResult<FetchAlbumListResult>
}