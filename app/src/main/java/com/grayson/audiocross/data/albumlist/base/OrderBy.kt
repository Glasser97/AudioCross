package com.grayson.audiocross.data.albumlist.base

import com.grayson.audiocross.domain.albumlist.base.OrderBy as DomainOrderBy

enum class OrderBy(val key: String) {
    CREATE_DATE("create_date"),
    RELEASE_DATE("release"),
    MY_RATING("rating"),
    DOWNLOAD_COUNT("dl_count"),
    SELLING_PRICE("price"),
    RATING("rate_average_2dp"),
    REVIEW_COUNT("review_count"),
    DLSITE_ID("id"),
    NO_NSFW("nsfw"),
    RANDOM("random");

    companion object {
        fun mapToDomain(orderBy: OrderBy): DomainOrderBy {
            return when (orderBy) {
                CREATE_DATE -> DomainOrderBy.CREATED_DATE
                RELEASE_DATE -> DomainOrderBy.RELEASE_DATE
                RATING -> DomainOrderBy.RATING
                REVIEW_COUNT -> DomainOrderBy.REVIEW_COUNT
                else -> DomainOrderBy.CREATED_DATE
            }
        }

        fun mapFromDomain(orderBy: DomainOrderBy): OrderBy {
            return when (orderBy) {
                DomainOrderBy.CREATED_DATE -> CREATE_DATE
                DomainOrderBy.RELEASE_DATE -> RELEASE_DATE
                DomainOrderBy.RATING -> RATING
                DomainOrderBy.REVIEW_COUNT -> REVIEW_COUNT
                DomainOrderBy.ID -> DLSITE_ID
                DomainOrderBy.RANDOM -> RANDOM
            }
        }
    }
}