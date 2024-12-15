package com.grayson.audiocross.presentation.search.model

import com.grayson.audiocross.domain.search.usecase.SearchAlbumListUseCase
import com.grayson.audiocross.presentation.albumlist.model.AlbumListFilterParam

fun AlbumListFilterParam.toRequestParam(page: Int, keywords: String? = null) = SearchAlbumListUseCase.Param(
    this.orderBy,
    this.sortMethod,
    page,
    this.hasSubtitle,
    keywords
)
