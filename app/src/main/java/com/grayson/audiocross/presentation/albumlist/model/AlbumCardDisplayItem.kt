package com.grayson.audiocross.presentation.albumlist.model

data class AlbumCardDisplayItem(
    val albumId: Long,
    val albumCode: String,
    val title: String,
    val voiceAuthor: String,
    val coverUrl: String,
    val duration: String,
)