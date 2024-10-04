package com.grayson.audiocross.domain.albumlist.usecase

import com.grayson.audiocross.domain.albumlist.model.FetchAlbumListResult
import com.grayson.audiocross.domain.albumlist.repository.IAlbumListRepository
import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.common.BaseUseCase
import com.grayson.audiocross.domain.common.RequestResult
import org.koin.java.KoinJavaComponent.inject

/**
 * Load Album List Use Case
 */
class FetchAlbumListUseCase :
    BaseUseCase<FetchAlbumListUseCase.Param, RequestResult<FetchAlbumListResult>>() {
    private val repository: IAlbumListRepository by inject(IAlbumListRepository::class.java)

    override suspend fun execute(param: Param): RequestResult<FetchAlbumListResult> {
        return repository.fetchAlbumList(param)
    }

    data class Param(
        val orderBy: OrderBy,
        val sortMethod: SortMethod,
        val page: Int,
        val hasSubtitle: Boolean
    )
}

