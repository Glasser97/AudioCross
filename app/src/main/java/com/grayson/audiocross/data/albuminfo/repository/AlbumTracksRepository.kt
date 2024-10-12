package com.grayson.audiocross.data.albuminfo.repository

import com.grayson.audiocross.data.albuminfo.mapper.toDomain
import com.grayson.audiocross.data.albumlist.clients.GetWorkTracksRequest
import com.grayson.audiocross.data.albumlist.clients.WorkClient
import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.albuminfo.repository.IAlbumTracksRepository
import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumTracksUseCase
import com.grayson.audiocross.domain.common.RequestResult

class AlbumTracksRepository : IAlbumTracksRepository {
    override suspend fun fetch(param: FetchAlbumTracksUseCase.Param): RequestResult<List<TrackItem>> {
        val result = WorkClient().getWorkTracks(GetWorkTracksRequest(param.albumId))
        when (result) {
            is RequestResult.Success -> {
                val data = result.data.mapNotNull { trackData ->
                    trackData.toDomain()
                }
                return RequestResult.Success(data)
            }

            is RequestResult.Error -> {
                return result
            }
        }
    }
}