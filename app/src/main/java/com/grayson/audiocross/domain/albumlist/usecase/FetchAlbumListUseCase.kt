package com.grayson.audiocross.domain.albumlist.usecase

import androidx.paging.PagingData
import com.grayson.audiocross.domain.albumlist.model.FetchAlbumListResult
import com.grayson.audiocross.domain.albumlist.repository.IAlbumListRepository
import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.common.RequestResult
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

/**
 * Load Album List Use Case
 */
class FetchAlbumListUseCase {
    private val repository: IAlbumListRepository by inject(IAlbumListRepository::class.java)

    fun fetch(param: Param): Flow<PagingData<AlbumItem>> {
        return repository.getAlbumItems(param)
    }

    data class Param(
        val orderBy: OrderBy,
        val sortMethod: SortMethod,
        val page: Int,
        val hasSubtitle: Boolean
    )
}

