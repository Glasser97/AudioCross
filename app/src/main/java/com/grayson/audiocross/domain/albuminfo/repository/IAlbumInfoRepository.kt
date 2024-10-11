package com.grayson.audiocross.domain.albuminfo.repository

import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumInfoUseCase
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.common.RequestResult

interface IAlbumInfoRepository {
    suspend fun fetchAlbumInfo(param: FetchAlbumInfoUseCase.Param): RequestResult<AlbumItem>
}