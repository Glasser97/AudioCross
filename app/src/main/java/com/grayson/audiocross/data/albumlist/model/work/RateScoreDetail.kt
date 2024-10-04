package com.grayson.audiocross.data.albumlist.model.work

import com.google.gson.annotations.SerializedName


data class RateScoreDetail(
    @SerializedName("count")
    val count: Int,
    @SerializedName("ratio")
    val ratio: Int,
    @SerializedName("review_point")
    val point: Int
) {
    override fun toString(): String {
        return "RateScoreDetail(count=$count, ratio=$ratio, point=$point)"
    }
}
