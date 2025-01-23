package com.grayson.audiocross.data.albumlist.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.grayson.audiocross.data.albumlist.datasource.AlbumListPageSource
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.albumlist.repository.IAlbumListRepository
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Fetch Album List repository implementation
 */
class AlbumListRepository : IAlbumListRepository {

    companion object {
        private const val TAG = "AlbumListRepository"

        private const val PAGE_SIZE = 10
    }

    override fun getAlbumItems(params: FetchAlbumListUseCase.Param): Flow<PagingData<AlbumItem>> {
        Log.d(TAG, "getAlbumItems, param: $params")
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                AlbumListPageSource(params)
            }).flow
    }
}