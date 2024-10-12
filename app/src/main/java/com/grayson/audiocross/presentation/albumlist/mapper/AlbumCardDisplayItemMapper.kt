package com.grayson.audiocross.presentation.albumlist.mapper

import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.presentation.albumlist.model.AlbumCardDisplayItem

fun AlbumItem.mapToDisplayItem(
    needSrcImage: Boolean = false
): AlbumCardDisplayItem {
    return AlbumCardDisplayItem(
        albumId = this.albumId,
        albumCode = this.albumCode,
        title = this.title,
        voiceAuthor = this.authorName.joinToString(separator = ", "),
        coverUrl = if (needSrcImage) this.cover.mainCoverUrl else this.cover.mediumCoverUrl,
        duration = transformToTimeString(this.duration)
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