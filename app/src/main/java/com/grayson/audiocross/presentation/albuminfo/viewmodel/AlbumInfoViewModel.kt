package com.grayson.audiocross.presentation.albuminfo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumInfoUseCase
import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumTracksUseCase
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.common.io
import com.grayson.audiocross.domain.exceptions.CommonError
import com.grayson.audiocross.domain.exceptions.RequestNotOkException
import com.grayson.audiocross.presentation.albuminfo.mapper.fillCoverUrl
import com.grayson.audiocross.presentation.albuminfo.mapper.fromDomain
import com.grayson.audiocross.presentation.albuminfo.model.TrackDisplayItem
import com.grayson.audiocross.presentation.albumlist.mapper.mapToDisplayItem
import com.grayson.audiocross.presentation.albumlist.model.AlbumCardDisplayItem
import com.grayson.audiocross.presentation.search.model.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class AlbumInfoViewModel(private val albumId: Long) : ViewModel() {

    companion object {
        private const val TAG = "AlbumInfoViewModel"
    }

    // region field

    private val useCaseSet = UseCaseSet()

    private val defaultAlbumInfo = AlbumCardDisplayItem(
        0L, "RJ101", "", "", "", ""
    )

    // endregion

    // region state

    private val _albumInfo = MutableStateFlow<AlbumCardDisplayItem>(defaultAlbumInfo)
    val albumInfo: StateFlow<AlbumCardDisplayItem> = _albumInfo


    private val _albumTracks = MutableStateFlow<List<TrackDisplayItem>>(emptyList())
    val albumTracks: StateFlow<List<TrackDisplayItem>> = _albumTracks

    private val _pageState = MutableStateFlow<UiState>(UiState.Init)
    val pageState: StateFlow<UiState> = _pageState

    // endregion

    // region public

    fun fetchData() {
        viewModelScope.launch {
            _pageState.update { UiState.Loading }
            val albumInfoDeferred = async {
                fetchAlbumInfo()
            }
            val tracksDeferred = async {
                fetchAlbumTracks()
            }

            val albumItemResult = albumInfoDeferred.await()
            val tracksResult = tracksDeferred.await()
            val successInAlbum = dealAlbumResult(albumItemResult)
            val successInTracks = dealWithTracksResult(tracksResult)
            if (successInAlbum && successInTracks) {
                _pageState.update { UiState.Success }
            } else {
                _pageState.update { UiState.Error }
            }
        }
    }

    private suspend fun fetchAlbumInfo(): RequestResult<AlbumItem> {
        return io {
            useCaseSet.fetchAlbumInfoUseCase.fetch(
                FetchAlbumInfoUseCase.Param(
                    albumId = albumId
                )
            )
        }
    }

    /**
     * @return true if request successful
     */
    private fun dealAlbumResult(result: RequestResult<AlbumItem>): Boolean {
        return when (result) {
            is RequestResult.Success -> {
                _albumInfo.update {
                    result.data.mapToDisplayItem(true)
                }
                true
            }

            is RequestResult.Error -> {
                Log.w(TAG, "fetchAlbumInfo error: ${result.exception}")
                false
            }
        }
    }

    private suspend fun fetchAlbumTracks(): RequestResult<List<TrackItem>> {
        if (albumId <= 0L) {
            Log.w(TAG, "fetchAlbumTracks error: albumId is invalid")
            return RequestResult.Error(RequestNotOkException(CommonError("albumId is invalid")))
        }
        return io {
            useCaseSet.fetchAlbumTracksUseCase.fetch(
                FetchAlbumTracksUseCase.Param(
                    albumId = albumId
                )
            )
        }
    }

    /**
     * @return true if request successful
     */
    private fun dealWithTracksResult(result: RequestResult<List<TrackItem>>): Boolean {
        return when (result) {
            is RequestResult.Success -> {
                _albumTracks.update {
                    result.data.map { it.fromDomain() }
                        .onEach { item -> item.fillCoverUrl(albumInfo.value.coverUrl) }
                }
                true
            }

            is RequestResult.Error -> {
                Log.w(TAG, "fetchAlbumTracks error: ${result.exception}")
                false
            }
        }
    }

    // endregion

    // region UseCase

    private class UseCaseSet {
        val fetchAlbumInfoUseCase: FetchAlbumInfoUseCase by inject(FetchAlbumInfoUseCase::class.java)
        val fetchAlbumTracksUseCase: FetchAlbumTracksUseCase by inject(FetchAlbumTracksUseCase::class.java)
    }

    // endregion
}