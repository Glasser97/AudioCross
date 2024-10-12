package com.grayson.audiocross.presentation.albumlist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.common.io
import com.grayson.audiocross.presentation.albumlist.mapper.mapToDisplayItem
import com.grayson.audiocross.presentation.albumlist.model.AlbumCardDisplayItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
 * Album List View Model
 */
class AlbumListViewModel : ViewModel() {

    // region constant

    companion object {
        private const val TAG = "AlbumListViewModel"
    }

    // endregion

    // region field

    private val useCaseSet = UseCaseSet()

    // endregion

    // region states

    /**
     * current page
     */
    private var page = 1

    /**
     * album list
     */
    private val _albumList = MutableStateFlow<List<AlbumCardDisplayItem>>(emptyList())
    val albumList: StateFlow<List<AlbumCardDisplayItem>> = _albumList
        .onStart { refreshAlbumList() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    /**
     * is Refreshing
     */
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    /**
     * loadMore State
     */
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore


    // endregion

    // region public

    fun refreshAlbumList() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            Log.i(TAG, "refreshAlbumList: start")
            val result = io {
                useCaseSet.fetchAlbumListUseCase.fetch(
                    FetchAlbumListUseCase.Param(
                        orderBy = OrderBy.RANDOM,
                        sortMethod = SortMethod.ASCENDING,
                        page = 1,
                        hasSubtitle = true
                    )
                )
            }
            when (result) {
                is RequestResult.Success -> {
                    _albumList.update {
                        result.data.albumItems.map {
                            it.mapToDisplayItem()
                        }
                    }
                    page = result.data.currentPage
                }

                is RequestResult.Error -> {
                    Log.w(TAG, "refreshAlbumList error: ${result.exception}")
                }
            }
            _isRefreshing.update { false }
            Log.i(TAG, "refreshAlbumList: end")
        }
    }

    fun loadMoreAlbumList() {
        viewModelScope.launch {
            _isLoadingMore.update { true }
            Log.i(TAG, "loadMoreAlbumList: start")
            val result = io {
                useCaseSet.fetchAlbumListUseCase.fetch(
                    FetchAlbumListUseCase.Param(
                        orderBy = OrderBy.RANDOM,
                        sortMethod = SortMethod.ASCENDING,
                        page = page + 1,
                        hasSubtitle = true
                    )
                )
            }
            when (result) {
                is RequestResult.Success -> {
                    _albumList.update {
                        val oldList = _albumList.value
                        oldList + result.data.albumItems.map {
                            it.mapToDisplayItem()
                        }
                    }
                    page = result.data.currentPage
                }

                is RequestResult.Error -> {
                    Log.w(TAG, "refreshAlbumList error: ${result.exception}")
                }
            }
            _isLoadingMore.update { false }
            Log.i(TAG, "loadMoreAlbumList: end")
        }
    }

    // endregion

    // region UseCase

    private class UseCaseSet {
        val fetchAlbumListUseCase: FetchAlbumListUseCase by inject(FetchAlbumListUseCase::class.java)
    }

    // endregion
}