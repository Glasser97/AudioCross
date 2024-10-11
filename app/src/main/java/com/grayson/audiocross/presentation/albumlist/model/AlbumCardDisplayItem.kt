package com.grayson.audiocross.presentation.albumlist.model

import com.grayson.audiocross.domain.albumlist.model.AlbumItem

data class AlbumCardDisplayItem(
    val albumId: Long,
    val title: String,
    val voiceAuthor: String,
    val coverUrl: String,
    val duration: String,
) {
    companion object {
        fun mapToDisplayItem(
            album: AlbumItem,
            needSrcImage: Boolean = false
        ): AlbumCardDisplayItem {
            return AlbumCardDisplayItem(
                albumId = album.albumId,
                title = album.title,
                voiceAuthor = album.authorName.joinToString(separator = ", "),
                coverUrl = if (needSrcImage) album.cover.mainCoverUrl else album.cover.mediumCoverUrl,
                duration = transformToTimeString(album.duration)
            )
        }

        fun transformToTimeString(duration: Long): String {
            val hour = duration / 3600
            val hourStr = if (hour < 10) "0$hour" else "$hour"
            val minute = (duration % 3600) / 60
            val minuteStr = if (minute < 10) "0$minute" else "$minute"
            val second = duration % 60
            val secondStr = if (second < 10) "0$second" else "$second"
            return "$hourStr:$minuteStr:$secondStr"

        }
    }
}