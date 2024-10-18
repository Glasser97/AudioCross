package com.grayson.audiocross.domain.albuminfo.model

sealed class TrackItem(val type: String) {
    data class Folder(
        val title: String, val children: List<TrackItem>
    ) : TrackItem(type = "folder")

    data class Audio(
        val hash: String,
        val title: String,
        val work: WorkItem?,
        val workTitle: String,
        val streamUrl: String,
        val streamLowQualityUrl: String,
        val downloadUrl: String,
        val fileSize: Long,
        val duration: Long
    ) : TrackItem(type = "audio")

    data class Text(
        val hash: String,
        val title: String,
        val work: WorkItem?,
        val workTitle: String,
        val streamUrl: String,
        val downloadUrl: String,
        val duration: Long,
        val fileSize: Long,
    ) : TrackItem(type = "text")
}

data class WorkItem(
    val id: Long, val sourceId: String, val sourceType: String, var coverUrl: String
)
