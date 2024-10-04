package com.grayson.audiocross.data.albumlist.base

import com.grayson.audiocross.domain.albumlist.base.SortMethod as DomainSortMethod

enum class SortMethod(val key: String) {
    ASCENDING("asc"),
    DESCENDING("desc");

    companion object {

        fun mapToDomain(sortMethod: SortMethod): DomainSortMethod {
            return when (sortMethod) {
                ASCENDING -> DomainSortMethod.ASCENDING
                DESCENDING -> DomainSortMethod.DESCENDING
            }
        }

        fun mapFromDomain(sortMethod: DomainSortMethod): SortMethod {
            return when (sortMethod) {
                DomainSortMethod.ASCENDING -> ASCENDING
                DomainSortMethod.DESCENDING -> DESCENDING
            }
        }
    }
}