package com.grayson.audiocross.domain.albuminfo.usecase

import com.grayson.audiocross.domain.albuminfo.repository.IAlbumInfoRepository
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.common.RequestResult
import org.koin.java.KoinJavaComponent.inject

class FetchAlbumInfoUseCase {
    private val repository: IAlbumInfoRepository by inject(IAlbumInfoRepository::class.java)

    suspend fun fetch(param: Param): RequestResult<AlbumItem> {
        return repository.fetchAlbumInfo(param)
    }

    data class Param(
        val albumId: Long
    )
}