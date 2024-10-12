package com.grayson.audiocross.data.albuminfo.model

import com.google.gson.annotations.SerializedName

data class AlbumTrackData(
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String?,
    @SerializedName("children") val children: List<AlbumTrackData>?,
    @SerializedName("hash") val hash: String?,
    @SerializedName("work") val work: WorkData?,
    @SerializedName("workTitle") val workTitle: String?,
    @SerializedName("mediaStreamUrl") val streamUrl: String?,
    @SerializedName("streamLowQualityUrl") val streamLowQualityUrl: String?,
    @SerializedName("mediaDownloadUrl") val downloadUrl: String?,
    @SerializedName("size") val fileSize: Long?,
    @SerializedName("duration") val duration: Double?
)

data class WorkData(
    @SerializedName("id") val id: Long,
    @SerializedName("source_id") val sourceId: String?,
    @SerializedName("source_type") val sourceType: String?
)