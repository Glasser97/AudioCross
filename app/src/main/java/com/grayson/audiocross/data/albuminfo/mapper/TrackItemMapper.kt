package com.grayson.audiocross.data.albuminfo.mapper

import com.grayson.audiocross.data.albuminfo.model.AlbumTrackData
import com.grayson.audiocross.data.albuminfo.model.WorkData
import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.albuminfo.model.WorkItem

fun AlbumTrackData.toDomain(): TrackItem? {
    return when (this.type) {
        "folder" -> {
            TrackItem.Folder(title = this.title ?: "",
                children = this.children?.mapNotNull { it.toDomain() } ?: emptyList())
        }

        "audio" -> {
            TrackItem.Audio(
                hash = this.hash ?: "",
                title = this.title ?: "",
                work = this.work?.toDomain(),
                workTitle = this.workTitle ?: "",
                streamUrl = this.streamUrl ?: "",
                streamLowQualityUrl = this.streamLowQualityUrl ?: "",
                downloadUrl = this.downloadUrl ?: "",
                fileSize = this.fileSize ?: 0L,
                duration = this.duration?.toLong() ?: 0L
            )
        }

        "text" -> {
            TrackItem.Text(
                hash = this.hash ?: "",
                title = this.title ?: "",
                work = this.work?.toDomain(),
                workTitle = this.workTitle ?: "",
                streamUrl = this.streamUrl ?: "",
                downloadUrl = this.downloadUrl ?: "",
                fileSize = this.fileSize ?: 0L,
                duration = this.duration?.toLong() ?: 0L
            )
        }

        else -> null
    }
}

fun WorkData.toDomain(): WorkItem? {
    return WorkItem(
        id = this.id, sourceId = this.sourceId ?: "", sourceType = this.sourceType ?: ""
    )
}