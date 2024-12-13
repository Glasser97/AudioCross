package com.grayson.audiocross.presentation.search.model

import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.search.usecase.SearchAlbumListUseCase

data class SearchFilterParam(
    val orderBy: OrderBy,
    val sortMethod: SortMethod,
    val hasSubtitle: Boolean,
    val keywords: String? = null
)

fun SearchFilterParam.toRequestParam(page: Int) = SearchAlbumListUseCase.Param(
    this.orderBy,
    this.sortMethod,
    page,
    this.hasSubtitle,
    this.keywords
)
