package com.grayson.audiocross.presentation.albuminfo.mapper

import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.presentation.albuminfo.model.TrackDisplayItem
import com.grayson.audiocross.presentation.albumlist.mapper.transformToTimeString


fun TrackItem.fromDomain(): TrackDisplayItem {
    when (this) {
        is TrackItem.Audio -> {
            return TrackDisplayItem.TrackAudioDisplayItem(
                domainData = this,
                title = this.title,
                hash = this.hash,
                albumTitle = this.workTitle,
                duration = transformToTimeString(this.duration),
                downloadUrl = this.downloadUrl,
                streamUrl = this.streamUrl,
                downsizeStreamUrl = this.streamLowQualityUrl,
                size = this.fileSize,
            )
        }

        is TrackItem.Text -> {
            return TrackDisplayItem.TrackTextDisplayItem(
                domainData = this,
                title = this.title,
                hash = this.hash,
                albumTitle = this.workTitle,
                downloadUrl = this.downloadUrl,
                streamUrl = this.streamUrl,
                size = this.fileSize
            )
        }

        is TrackItem.Folder -> {
            return TrackDisplayItem.TrackFolderDisplayItem(
                domainData = this,
                title = this.title,
                children = this.children.map { it.fromDomain() }
            )
        }
    }
}