package com.grayson.audiocross.domain.albumlist.model

data class AlbumItem(
    val albumId: Long,
    val albumCode: String,
    val title: String,
    val duration: Long,
    val authorName: List<String>,
    val cover: AlbumCover
)