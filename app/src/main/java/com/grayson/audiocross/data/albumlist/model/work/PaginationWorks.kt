package com.grayson.audiocross.data.albumlist.model.work

import com.google.gson.annotations.SerializedName

data class PaginationWorks(
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("works")
    val works: Array<WorkInfo>
) {
    override fun toString(): String {
        return "PaginationWorks(pagination=$pagination, works=${works.contentToString()})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaginationWorks

        if (pagination != other.pagination) return false
        if (!works.contentEquals(other.works)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pagination.hashCode()
        result = 31 * result + works.contentHashCode()
        return result
    }
}