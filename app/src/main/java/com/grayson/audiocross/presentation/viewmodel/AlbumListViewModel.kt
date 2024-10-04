package com.grayson.audiocross.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.common.io
import com.grayson.audiocross.presentation.AlbumCardDisplayItem
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
    private var page = 0

    /**
     * album list
     */
    private val _albumList = MutableStateFlow<List<AlbumCardDisplayItem>>(emptyList())
    val albumList: StateFlow<List<AlbumCardDisplayItem>> = _albumList
        .onStart { refreshAlbumList() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(10_000),
            initialValue = emptyList()
        )

    /**
     * is Refreshing
     */
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    // endregion

    // region public

    fun refreshAlbumList() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            val result = io {
                useCaseSet.fetchAlbumListUseCase.execute(
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
                        result.data.albumItems.map {
                            AlbumCardDisplayItem.mapToDisplayItem(it)
                        }
                    }
                    page = result.data.currentPage
                }

                is RequestResult.Error -> {
                    Log.w(TAG, "refreshAlbumList error: ${result.exception}")
                }
            }
            _isRefreshing.update { false }
        }
    }

    // endregion

    // region UseCase

    private class UseCaseSet {
        val fetchAlbumListUseCase: FetchAlbumListUseCase by inject(FetchAlbumListUseCase::class.java)
    }

    // endregion
}