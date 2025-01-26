package com.grayson.audiocross.domain.albumlist.repository

import androidx.paging.PagingSource
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase

/**
 * Fetch Album List repository interface
 */
interface IAlbumListRepository {
    fun getPagerSource(params: FetchAlbumListUseCase.Param): PagingSource<FetchAlbumListUseCase.Param, AlbumItem>
}