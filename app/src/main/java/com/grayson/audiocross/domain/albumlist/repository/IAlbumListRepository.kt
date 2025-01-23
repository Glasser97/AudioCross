package com.grayson.audiocross.domain.albumlist.repository

import androidx.paging.PagingData
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Fetch Album List repository interface
 */
interface IAlbumListRepository {
    fun getAlbumItems(params: FetchAlbumListUseCase.Param): Flow<PagingData<AlbumItem>>
}