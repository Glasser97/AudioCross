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
import com.grayson.audiocross.presentation.search.model.ListState
import com.grayson.audiocross.presentation.search.model.UiState
import com.grayson.audiocross.presentation.search.model.toRequestParam
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
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

        private const val SEARCH_DEBOUNCE_TIME = 1000L
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
    val albumList: StateFlow<List<AlbumCardDisplayItem>> =_albumList

    /**
     * request filter param (not include page).
     */
    private val _filterParam = MutableStateFlow(
        AlbumListFilterParam(
            orderBy = OrderBy.REVIEW_COUNT, sortMethod = SortMethod.DESCENDING, hasSubtitle = false
        )
    )
    val filterParam: StateFlow<AlbumListFilterParam> = _filterParam

    /**
     * search keywords
     */
    private val _keywords = MutableStateFlow("")
    val keywords: StateFlow<String> = _keywords

    /**
     * Page State
     */
    private val _listState: MutableStateFlow<ListState> =
        MutableStateFlow(
            ListState(
                refreshState = UiState.Init,
                loadMoreState = UiState.Init
            )
        )
    val listState: StateFlow<ListState> = _listState

    /**
     * request job
     */
    private var requestJob: Job? = null

    // endregion

    // region init

    init {
        viewModelScope.launch {
            combine(keywords, filterParam) { keywords, filterParam ->
                Pair(keywords, filterParam)
            }.drop(1).debounce(SEARCH_DEBOUNCE_TIME).distinctUntilChanged().collect {
                refreshAlbumList()
            }
        }
    }

    // endregion

    // region public

    fun refreshAlbumList() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch {
            Log.i(TAG, "refreshAlbumList: start")
            _listState.update { _listState.value.copy(refreshState = UiState.Loading) }
            val result = io {
                useCaseSet.searchAlbumListUseCase.fetch(
                    param = filterParam.value.toRequestParam(page = 1, keywords = keywords.value)
                )
            }
            when (result) {
                is RequestResult.Success -> {
                    _listState.update { _listState.value.copy(refreshState = UiState.Success) }
                    _albumList.update {
                        result.data.albumItems.map {
                            it.mapToDisplayItem(true)
                        }
                    }
                    page = result.data.currentPage
                }

                is RequestResult.Error -> {
                    _listState.update { _listState.value.copy(refreshState = UiState.Error) }
                    Log.w(TAG, "refreshAlbumList error: ${result.exception}")
                }
            }
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
        if (listState.value.refreshState !is UiState.Success) {
            Log.i(TAG, "loadMoreAlbumList: return because refreshState is not Success")
            return
        }
        if (listState.value.loadMoreState is UiState.Loading) {
            Log.i(TAG, "loadMoreAlbumList: return because pageState is loading")
            return
        }
        requestJob?.cancel()
        requestJob = viewModelScope.launch {
            Log.i(TAG, "loadMoreAlbumList: start")
            _listState.update {
                _listState.value.copy(loadMoreState = UiState.Loading)
            }
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
                    _listState.update {
                        _listState.value.copy(loadMoreState = UiState.Success)
                    }
                }

                is RequestResult.Error -> {
                    _listState.update {
                        _listState.value.copy(loadMoreState = UiState.Error)
                    }
                    Log.w(TAG, "refreshAlbumList error: ${result.exception}")
                }
            }
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