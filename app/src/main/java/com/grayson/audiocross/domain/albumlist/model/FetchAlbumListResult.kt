package com.grayson.audiocross.domain.albumlist.model


/**
 * Load Album List Result
 */
data class FetchAlbumListResult(
    val albumItems: List<AlbumItem>,
    val currentPage: Int,
    val pageSize: Int,
    val totalCount: Int
)
