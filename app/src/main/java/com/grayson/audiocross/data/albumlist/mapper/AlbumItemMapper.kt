package com.grayson.audiocross.data.albumlist.mapper

import com.grayson.audiocross.data.albumlist.model.work.WorkInfo
import com.grayson.audiocross.domain.albumlist.model.AlbumCover
import com.grayson.audiocross.domain.albumlist.model.AlbumItem

fun WorkInfo.mapToDomain(): AlbumItem =
    AlbumItem(
        albumId = this.id,
        title = this.title,
        albumCode = this.sourceId,
        authorName = this.vases.map { vas -> vas.name },
        cover = AlbumCover(
            mainCoverUrl = this.mainCoverUrl,
            mediumCoverUrl = this.samCoverUrl,
            thumbnailCoverUrl = this.thumbnailCoverUrl
        ),
        duration = this.duration
    )
