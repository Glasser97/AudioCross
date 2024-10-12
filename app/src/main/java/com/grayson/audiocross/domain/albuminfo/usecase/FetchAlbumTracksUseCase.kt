package com.grayson.audiocross.domain.albuminfo.usecase

import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.albuminfo.repository.IAlbumTracksRepository
import com.grayson.audiocross.domain.common.RequestResult
import org.koin.java.KoinJavaComponent.inject

class FetchAlbumTracksUseCase {
    private val repository: IAlbumTracksRepository by inject(IAlbumTracksRepository::class.java)

    suspend fun fetch(param: Param): RequestResult<List<TrackItem>> {
        return repository.fetch(param)
    }

    data class Param(
        val albumId: Long
    )
}