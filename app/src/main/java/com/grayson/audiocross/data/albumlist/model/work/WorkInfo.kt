package com.grayson.audiocross.data.albumlist.model.work

import com.google.gson.annotations.SerializedName
import com.grayson.audiocross.data.albumlist.model.circle.Circle
import com.grayson.audiocross.data.albumlist.model.vas.Vas

data class WorkInfo(
    @SerializedName("circle")
    val circle: Circle,
    @SerializedName("circle_id")
    val circleId: Int,
    @SerializedName("create_date")
    val createDateString: String,
    @SerializedName("dl_count")
    val downloadCount: Int,
    @SerializedName("has_subtitle")
    val hasSubtitle: Boolean,
    @SerializedName("id")
    val id: Long,
    @SerializedName("source_id")
    val sourceId: String,
    val mainCoverUrl: String,
    val samCoverUrl: String,
    val thumbnailCoverUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("nsfw")
    val isNSFW: Boolean,
    @SerializedName("price")
    val sellingPrice: Int,
    @SerializedName("rank")
    val historyRanks: Array<Rank>?,
    @SerializedName("rate_average_2dp")
    val rateAvgScore: Double,
    @SerializedName("rate_count")
    val rateCount: Int,
    @SerializedName("rate_count_detail")
    val rateDetailByScore: Array<RateScoreDetail>,
    @SerializedName("release")
    val releaseDateString: String,
    @SerializedName("review_count")
    val commentCount: Int,
    @SerializedName("tags")
    val tags: Array<Tag>?,
    @SerializedName("title")
    val title: String,
    @SerializedName("vas")
    val vases: Array<Vas>,
    @SerializedName("userRating")
    val rateByUser: Int?,
    @SerializedName("progress")
    val progress: String?,
    @SerializedName("user_name")
    val username: String?,
    @SerializedName("duration")
    val duration: Long
) {
    override fun toString(): String {
        return "WorkInfo(circle=$circle, circleId=$circleId, createDateString='$createDateString', downloadCount=$downloadCount, hasSubtitle=$hasSubtitle, id=$id, mainCoverUrl='$mainCoverUrl', samCoverUrl='$samCoverUrl', thumbnailCoverUrl='$thumbnailCoverUrl', name='$name', isNSFW=$isNSFW, sellingPrice=$sellingPrice, historyRanks=${historyRanks?.contentToString()}, rateAvgScore=$rateAvgScore, rateCount=$rateCount, rateDetailByScore=${rateDetailByScore.contentToString()}, releaseDateString='$releaseDateString', commentCount=$commentCount, tags=${tags?.contentToString()}, title='$title', vases=${vases.contentToString()}, rateByUser=$rateByUser, progress=$progress, username='$username')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkInfo

        if (circle != other.circle) return false
        if (circleId != other.circleId) return false
        if (createDateString != other.createDateString) return false
        if (downloadCount != other.downloadCount) return false
        if (hasSubtitle != other.hasSubtitle) return false
        if (id != other.id) return false
        if (sourceId != other.sourceId) return false
        if (mainCoverUrl != other.mainCoverUrl) return false
        if (samCoverUrl != other.samCoverUrl) return false
        if (thumbnailCoverUrl != other.thumbnailCoverUrl) return false
        if (name != other.name) return false
        if (isNSFW != other.isNSFW) return false
        if (sellingPrice != other.sellingPrice) return false
        if (historyRanks != null) {
            if (other.historyRanks == null) return false
            if (!historyRanks.contentEquals(other.historyRanks)) return false
        } else if (other.historyRanks != null) return false
        if (rateAvgScore != other.rateAvgScore) return false
        if (rateCount != other.rateCount) return false
        if (!rateDetailByScore.contentEquals(other.rateDetailByScore)) return false
        if (releaseDateString != other.releaseDateString) return false
        if (commentCount != other.commentCount) return false
        if (tags != null) {
            if (other.tags == null) return false
            if (!tags.contentEquals(other.tags)) return false
        } else if (other.tags != null) return false
        if (title != other.title) return false
        if (!vases.contentEquals(other.vases)) return false
        if (rateByUser != other.rateByUser) return false
        if (progress != other.progress) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = circle.hashCode()
        result = 31 * result + circleId
        result = 31 * result + createDateString.hashCode()
        result = 31 * result + downloadCount
        result = 31 * result + hasSubtitle.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + mainCoverUrl.hashCode()
        result = 31 * result + samCoverUrl.hashCode()
        result = 31 * result + thumbnailCoverUrl.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + isNSFW.hashCode()
        result = 31 * result + sellingPrice
        result = 31 * result + (historyRanks?.contentHashCode() ?: 0)
        result = 31 * result + rateAvgScore.hashCode()
        result = 31 * result + rateCount
        result = 31 * result + rateDetailByScore.contentHashCode()
        result = 31 * result + releaseDateString.hashCode()
        result = 31 * result + commentCount
        result = 31 * result + (tags?.contentHashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + vases.contentHashCode()
        result = 31 * result + (rateByUser ?: 0)
        result = 31 * result + (progress?.hashCode() ?: 0)
        result = 31 * result + username.hashCode()
        result = 31 * result + duration.hashCode()
        return result
    }
}
