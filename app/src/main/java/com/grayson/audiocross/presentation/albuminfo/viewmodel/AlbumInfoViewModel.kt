package com.grayson.audiocross.presentation.albuminfo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumInfoUseCase
import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumTracksUseCase
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.common.io
import com.grayson.audiocross.presentation.albuminfo.mapper.fromDomain
import com.grayson.audiocross.presentation.albuminfo.model.TrackDisplayItem
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

class AlbumInfoViewModel(private val albumId: Long) : ViewModel() {

    companion object {
        private const val TAG = "AlbumInfoViewModel"
    }

    // region field

    private val useCaseSet = UseCaseSet()

    private val defaultAlbumInfo = AlbumCardDisplayItem(
        0L,"RJ101", "", "", "", ""
    )

    // endregion

    // region state

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _albumInfo = MutableStateFlow<AlbumCardDisplayItem>(defaultAlbumInfo)
    val albumInfo: StateFlow<AlbumCardDisplayItem> = _albumInfo.onStart {
        if (_albumInfo.value.albumId <= 0L) {
            fetchAlbumInfo()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = defaultAlbumInfo
    )


    private val _albumTracks = MutableStateFlow<List<TrackDisplayItem>>(emptyList())
    val albumTracks: StateFlow<List<TrackDisplayItem>> = _albumTracks
        .onStart {
            if (_albumTracks.value.isEmpty()) {
                fetchAlbumTracks()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    // endregion

    // region public

    fun fetchAlbumInfo() {
        viewModelScope.launch {

            val result = io {
                useCaseSet.fetchAlbumInfoUseCase.fetch(
                    FetchAlbumInfoUseCase.Param(
                        albumId = albumId
                    )
                )
            }
            when (result) {
                is RequestResult.Success -> {
                    _albumInfo.update {
                        result.data.mapToDisplayItem(true)
                    }
                }

                is RequestResult.Error -> {
                    Log.w(TAG, "fetchAlbumInfo error: ${result.exception}")
                }
            }
        }
    }

    fun fetchAlbumTracks() {
        viewModelScope.launch {
            val result = io {
                useCaseSet.fetchAlbumTracksUseCase.fetch(
                    FetchAlbumTracksUseCase.Param(
                        albumId = albumId
                    )
                )
            }
            when (result) {
                is RequestResult.Success -> {
                    _albumTracks.update {
                        result.data.map { it.fromDomain() }
                    }
                }

                is RequestResult.Error -> {
                    Log.w(TAG, "fetchAlbumTracks error: ${result.exception}")
                }
            }
        }
    }

    fun playOrPauseAudio(audio: TrackDisplayItem.TrackAudioDisplayItem) {

    }

    // endregion

    // region UseCase

    private class UseCaseSet {
        val fetchAlbumInfoUseCase: FetchAlbumInfoUseCase by inject(FetchAlbumInfoUseCase::class.java)
        val fetchAlbumTracksUseCase: FetchAlbumTracksUseCase by inject(FetchAlbumTracksUseCase::class.java)
    }

    // endregion
}