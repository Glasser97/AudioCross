package com.grayson.audiocross.domain.albuminfo.repository

import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumTracksUseCase
import com.grayson.audiocross.domain.common.RequestResult

interface IAlbumTracksRepository {
    suspend fun fetch(param: FetchAlbumTracksUseCase.Param): RequestResult<List<TrackItem>>
}