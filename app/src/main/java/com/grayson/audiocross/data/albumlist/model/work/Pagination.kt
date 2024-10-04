package com.grayson.audiocross.data.albumlist.model.work

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("totalCount")
    val totalCount: Int
) {
    override fun toString(): String {
        return "Pagination(currentPage=$currentPage, pageSize=$pageSize, totalCount=$totalCount)"
    }
}