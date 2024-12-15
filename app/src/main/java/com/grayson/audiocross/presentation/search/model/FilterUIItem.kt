package com.grayson.audiocross.presentation.search.model

import androidx.annotation.StringRes
import com.grayson.audiocross.R
import com.grayson.audiocross.domain.albumlist.base.OrderBy

/**
 * Filter UI Item
 */
enum class FilterUIItem(
    val orderBy: OrderBy,
    @StringRes val description: Int,
) {
    CreatedDate(
        OrderBy.CREATED_DATE,
        R.string.filter_created_date
    ),
    ReleaseDate(
        OrderBy.RELEASE_DATE,
        R.string.filter_release_date
    ),
    Id(
        OrderBy.ID,
        R.string.filter_id
    ),
    Rating(
        OrderBy.RATING,
        R.string.filter_rating
    ),
    Random(
        OrderBy.RANDOM,
        R.string.filter_random
    ),
    ReviewCount(
        OrderBy.REVIEW_COUNT,
        R.string.filter_review_count
    );

    companion object {
        val filterUIItems: List<FilterUIItem> = entries.toList()
    }
}