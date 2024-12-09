package com.grayson.audiocross.presentation.albumlist.model

import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase

data class FilterParam(
    val orderBy: OrderBy,
    val sortMethod: SortMethod,
    val hasSubtitle: Boolean,
    val keywords: String? = null
)

fun FilterParam.toRequestParam(page: Int) = FetchAlbumListUseCase.Param(
    this.orderBy,
    this.sortMethod,
    page,
    this.hasSubtitle,
    this.keywords
)
