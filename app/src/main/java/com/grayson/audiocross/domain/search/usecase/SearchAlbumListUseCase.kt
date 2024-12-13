package com.grayson.audiocross.domain.search.usecase

import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.albumlist.model.FetchAlbumListResult
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.search.repository.ISearchAlbumRepository
import org.koin.java.KoinJavaComponent.inject

class SearchAlbumListUseCase {
    private val repository: ISearchAlbumRepository by inject(ISearchAlbumRepository::class.java)

    suspend fun fetch(param: Param): RequestResult<FetchAlbumListResult> {
        return repository.searchAlbumList(param)
    }

    data class Param(
        val orderBy: OrderBy,
        val sortMethod: SortMethod,
        val page: Int,
        val hasSubtitle: Boolean,
        val keywords: String? = null
    )
}