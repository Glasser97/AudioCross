package com.grayson.audiocross.data.albumlist.repository

import android.util.Log
import androidx.paging.PagingSource
import com.grayson.audiocross.data.albumlist.datasource.AlbumListPageSource
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.albumlist.repository.IAlbumListRepository
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase

/**
 * Fetch Album List repository implementation
 */
class AlbumListRepository : IAlbumListRepository {

    companion object {
        private const val TAG = "AlbumListRepository"
    }

    override fun getPagerSource(params: FetchAlbumListUseCase.Param): PagingSource<FetchAlbumListUseCase.Param, AlbumItem> {
        Log.d(TAG, "getPagerSource, param: $params")
        return AlbumListPageSource(params)
    }
}