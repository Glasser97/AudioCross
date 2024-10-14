package com.grayson.audiocross.presentation.albuminfo.model

import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import kotlinx.coroutines.flow.MutableStateFlow


sealed class TrackDisplayItem(
    val type: String,
    open val title: String,
) {
    data class TrackFolderDisplayItem(
        val domainData: TrackItem.Folder,
        override val title: String,
        val children: List<TrackDisplayItem>
    ) : TrackDisplayItem(
        type = "folder", title = title
    ) {
        val isExpanded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    }


    data class TrackTextDisplayItem(
        val domainData: TrackItem.Text,
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
        val domainData: TrackItem.Audio,
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