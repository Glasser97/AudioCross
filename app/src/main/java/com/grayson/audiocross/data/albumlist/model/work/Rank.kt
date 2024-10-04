package com.grayson.audiocross.data.albumlist.model.work

import com.google.gson.annotations.SerializedName

data class Rank(
    @SerializedName("category")
    val category: String,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("rank_date")
    val rankDate: String,
    @SerializedName("term")
    val term: String
) {
    override fun toString(): String {
        return "Rank(category='$category', rank=$rank, rankDate='$rankDate', term='$term')"
    }
}
