package com.grayson.audiocross.presentation.search.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.common.io
import com.grayson.audiocross.domain.search.usecase.SearchAlbumListUseCase
import com.grayson.audiocross.presentation.albumlist.mapper.mapToDisplayItem
import com.grayson.audiocross.presentation.albumlist.model.AlbumCardDisplayItem
import com.grayson.audiocross.presentation.albumlist.model.AlbumListFilterParam
import com.grayson.audiocross.presentation.search.model.toRequestParam
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
 * Search Album View Model
 */
@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {

    // region constant

    companion object {
        private const val TAG = "SearchViewModel"

        private const val SEARCH_DEBOUNCE_TIME = 1500L
    }

    // endregion

    // region field

    private val useCaseSet = UseCaseSet()

    // endregion

    // region state

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

    /**
     * request filter param (not include page).
     */
    private val _filterParam = MutableStateFlow(
        AlbumListFilterParam(
            orderBy = OrderBy.CREATED_DATE,
            sortMethod = SortMethod.DESCENDING,
            hasSubtitle = false
        )
    )
    val filterParam: StateFlow<AlbumListFilterParam> = _filterParam

    /**
     * search keywords
     */
    private val _keywords = MutableStateFlow("")
    val keywords: StateFlow<String> = _keywords

    // endregion

    // region init

    init {
        viewModelScope.launch {
            _keywords
                .debounce(SEARCH_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect {
                    refreshAlbumList()
                }
        }
        viewModelScope.launch {
            _filterParam.collect { param ->
                refreshAlbumList()
            }
        }
    }

    // endregion

    // region public

    fun refreshAlbumList() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            Log.i(TAG, "refreshAlbumList: start")
            val result = io {
                useCaseSet.searchAlbumListUseCase.fetch(
                    param = filterParam.value.toRequestParam(page = 1, keywords = keywords.value)
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

    fun updateKeywords(keywords: String) {
        _keywords.update {
            keywords
        }
    }

    fun updateFilterParam(param: AlbumListFilterParam) {
        _filterParam.update {
            param
        }
    }

    fun loadMoreAlbumList() {
        viewModelScope.launch {
            _isLoadingMore.update { true }
            Log.i(TAG, "loadMoreAlbumList: start")
            val result = io {
                useCaseSet.searchAlbumListUseCase.fetch(
                    param = filterParam.value.toRequestParam(page + 1, keywords.value)
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
        val searchAlbumListUseCase: SearchAlbumListUseCase by inject(SearchAlbumListUseCase::class.java)
    }

    // endregion
}