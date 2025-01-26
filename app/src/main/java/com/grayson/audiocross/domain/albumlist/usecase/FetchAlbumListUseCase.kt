package com.grayson.audiocross.domain.albumlist.usecase

import androidx.paging.PagingSource
import com.grayson.audiocross.domain.albumlist.repository.IAlbumListRepository
import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import org.koin.java.KoinJavaComponent.inject

/**
 * Load Album List Use Case
 */
class FetchAlbumListUseCase {
    private val repository: IAlbumListRepository by inject(IAlbumListRepository::class.java)

    fun fetch(param: Param): PagingSource<Param, AlbumItem> {
        return repository.getPagerSource(param)
    }

    data class Param(
        val orderBy: OrderBy,
        val sortMethod: SortMethod,
        val page: Int,
        val hasSubtitle: Boolean
    )
}

