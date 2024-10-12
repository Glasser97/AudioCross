package com.grayson.audiocross.presentation.albuminfo.model

import kotlinx.coroutines.flow.MutableStateFlow


sealed class TrackDisplayItem(
    val type: String,
    open val title: String,
) {
    data class TrackFolderDisplayItem(
        override val title: String, val children: List<TrackDisplayItem>
    ) : TrackDisplayItem(
        type = "folder", title = title
    ) {
        val isExpanded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    }


    data class TrackTextDisplayItem(
        override val title: String,
        val hash: String,
        val albumTitle: String,
        val streamUrl: String,
        val downloadUrl: String,
        val size: Long
    ) : TrackDisplayItem(
        type = "text", title = title
    )


    data class TrackAudioDisplayItem(
        override val title: String,
        val hash: String,
        val albumTitle: String,
        val duration: String,
        val size: Long,
        val streamUrl: String,
        val downsizeStreamUrl: String,
        val downloadUrl: String,
    ) : TrackDisplayItem(
        type = "audio", title = title
    ) {
        val isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    }
}