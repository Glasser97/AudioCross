package com.grayson.audiocross.presentation.albumlist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import com.grayson.audiocross.presentation.albumlist.mapper.mapToDisplayItem
import com.grayson.audiocross.presentation.albumlist.model.AlbumListFilterParam
import com.grayson.audiocross.presentation.albumlist.model.toRequestParam
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.java.KoinJavaComponent.inject

/**
 * Album List View Model
 */
class AlbumListViewModel : ViewModel() {

    // region constant

    companion object {
        private const val TAG = "AlbumListViewModel"

        private const val PAGE_SIZE = 10
    }

    // endregion

    // region field

    private val useCaseSet = UseCaseSet()

    // endregion

    // region states

    /**
     * request filter param (not include page).
     */
    private val _filterParam = MutableStateFlow(
        AlbumListFilterParam(
            orderBy = OrderBy.CREATED_DATE, sortMethod = SortMethod.DESCENDING, hasSubtitle = true
        )
    )
    val filterParam: StateFlow<AlbumListFilterParam> = _filterParam

    /**
     * Paging Flow for display item
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow = filterParam.flatMapLatest { filters ->
        useCaseSet.fetchAlbumListUseCase.fetch(filters.toRequestParam(page = 1))
            .map {
                it.map { item ->
                    item.mapToDisplayItem(true)
                }
            }
    }.cachedIn(viewModelScope)

    // endregion

    // region init

    init {
        Log.d(TAG, "viewModel init")
    }

    // endregion

    // region UseCase

    private class UseCaseSet {
        val fetchAlbumListUseCase: FetchAlbumListUseCase by inject(FetchAlbumListUseCase::class.java)
    }

    // endregion
}