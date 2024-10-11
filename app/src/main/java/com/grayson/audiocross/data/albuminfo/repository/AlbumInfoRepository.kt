package com.grayson.audiocross.data.albuminfo.repository

import com.grayson.audiocross.data.albumlist.clients.GetWorkInfoRequest
import com.grayson.audiocross.data.albumlist.clients.WorkClient
import com.grayson.audiocross.domain.albuminfo.repository.IAlbumInfoRepository
import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumInfoUseCase
import com.grayson.audiocross.domain.albumlist.model.AlbumCover
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.common.RequestResult

class AlbumInfoRepository : IAlbumInfoRepository {
    override suspend fun fetchAlbumInfo(param: FetchAlbumInfoUseCase.Param): RequestResult<AlbumItem> {
        return when (val dataResult = WorkClient().getWorkInfo(GetWorkInfoRequest(param.albumId))) {
            is RequestResult.Success -> {
                val workInfo = dataResult.data
                val albumItem = AlbumItem(
                    albumId = workInfo.id,
                    title = workInfo.title,
                    authorName = workInfo.vases.map { vas -> vas.name },
                    cover = AlbumCover(
                        mainCoverUrl = workInfo.mainCoverUrl,
                        mediumCoverUrl = workInfo.samCoverUrl,
                        thumbnailCoverUrl = workInfo.thumbnailCoverUrl
                    ),
                    duration = workInfo.duration
                )
                RequestResult.Success(albumItem)
            }

            is RequestResult.Error -> dataResult
        }
    }
}