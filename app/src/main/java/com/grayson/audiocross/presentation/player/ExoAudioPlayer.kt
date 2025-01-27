package com.grayson.audiocross.presentation.player

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.albuminfo.model.WorkItem
import com.grayson.audiocross.domain.player.IAudioPlayer
import com.grayson.audiocross.domain.player.PlaybackSpeed
import com.grayson.audiocross.domain.player.PlayerState
import com.grayson.audiocross.domain.player.PlayingState
import com.grayson.audiocross.domain.player.hasNext
import com.grayson.audiocross.domain.player.hasPrevious
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.reflect.KProperty


/**
 * This singleton Player represents the interface between the application and the media playback service running in the background
 * It expose the current state of the MediaSessionService as state flows so UI can update accordingly
 */
class ExoAudioPlayer(
    private val context: Context
) : IAudioPlayer {

    // region constant

    companion object {
        private const val TAG = "ExoAudioPlayer"
    }

    // endregion

    // region field

    private val _playerState = MutableStateFlow(PlayerState.Empty)

    override val playerState: StateFlow<PlayerState> = _playerState

    // region media controller

    private val _isReady = MutableStateFlow(false)

    private val sessionToken =
        SessionToken(context, ComponentName(context, AudioService::class.java))

    private var mediaController: MediaController? = null

    private val mediaControllerFuture =
        MediaController.Builder(context, sessionToken).buildAsync().apply {
            this.addListener({
                if (this.isDone) {
                    _isReady.update { true }
                    mediaController = this.get()
                    mediaController?.addListener(ExoPlayerListener())
                    updateState()
                }
            }, MoreExecutors.directExecutor())
        }

    // endregion

    // endregion

    // region private

    /**
     * update player state
     */
    override fun updateState() {
        val currentMediaItem = mediaController?.currentMediaItem ?: return updateToEmptyState()
        val currentAudio = currentMediaItem.toAudio() ?: return updateToEmptyState()
        val newState = PlayerState(
            currentAudio = currentAudio,
            playQueue = _playerState.value.playQueue,
            playingState = if (mediaController?.isPlaying == true) PlayingState.PLAYING else PlayingState.PAUSED,
            currentPosition = mediaController?.currentPosition ?: 0,
            playbackSpeed = PlaybackSpeed.fromFSpeed(
                mediaController?.playbackParameters?.speed ?: 1f
            ),
            duration = mediaController?.duration ?: 0
        )
        Log.d(TAG, "updateState: $newState")
        _playerState.update { newState }
    }

    private fun updateToEmptyState() {
        _playerState.update { PlayerState.Empty }
    }

    // endregion

    // region override function

    override fun addToQueue(audio: TrackItem.Audio) {
        _playerState.update {
            it.copy(playQueue = it.playQueue + audio)
        }
        mediaController?.addMediaItem(audio.toMediaItem())
    }

    override fun removeAllFromQueue() {
        _playerState.update {
            it.copy(playQueue = emptyList())
        }
        mediaController?.clearMediaItems()
    }

    override fun play() {
        mediaController?.play()
    }

    override fun play(audio: TrackItem.Audio) {
        play(listOf(audio))
    }

    override fun play(audios: List<TrackItem.Audio>, index: Int) {
        _playerState.update {
            it.copy(playQueue = audios)
        }
        val mediaItems = audios.map { it.toMediaItem() }
        mediaController?.clearMediaItems()
        mediaController?.setMediaItems(mediaItems)
        mediaController?.seekToDefaultPosition(index.coerceIn(0, mediaItems.size - 1))
        mediaController?.prepare()
        mediaController?.play()
    }

    override fun pause() {
        if (mediaController?.isPlaying == true) {
            mediaController?.pause()
        }
    }

    override fun stop() {
        if (mediaController?.isPlaying == true) {
            mediaController?.stop()
        }
    }

    override fun next() {
        if (playerState.value.hasNext()) {
            mediaController?.seekToNext()
        }
    }

    override fun previous() {
        if (playerState.value.hasPrevious()) {
            mediaController?.seekToPrevious()
        }
    }

    override fun advanceBy(duration: Long) {
        val currentPosition = mediaController?.currentPosition ?: 0
        if (currentPosition + duration < (mediaController?.duration ?: 0)) {
            mediaController?.seekTo(currentPosition + duration)
        }
    }

    override fun rewindBy(duration: Long) {
        val currentPosition = mediaController?.currentPosition ?: 0
        if (currentPosition - duration >= 0) {
            mediaController?.seekTo(currentPosition - duration)
        }
    }

    override fun changePlaybackSpeed(speed: PlaybackSpeed) {
        if (speed.speed != mediaController?.playbackParameters?.speed) {
            mediaController?.setPlaybackSpeed(speed.speed)
        }
    }

    override fun onSeekingStarted() {
        pause()
    }

    override fun onSeekingFinished(duration: Long) {
        val currentAudioDuration = playerState.value.duration
        duration.coerceIn(0, currentAudioDuration)
        mediaController?.seekTo(duration)
        mediaController?.play()
    }

    // endregion

    // region PLayer.Listener

    inner class ExoPlayerListener : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            Log.d(TAG, "onIsPlayingChanged: $isPlaying")
            updateState()
        }

        override fun onPlayWhenReadyChanged(
            playWhenReady: Boolean,
            reason: Int
        ) {
            super.onPlayWhenReadyChanged(playWhenReady, reason)
            Log.d(TAG, "onPlayWhenReadyChanged: $playWhenReady")
            updateState()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            Log.d(TAG, "onPlaybackStateChanged: $playbackState")
            updateState()
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Log.d(TAG, "onPlayerError: $error")
            updateToEmptyState()
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            super.onPositionDiscontinuity(oldPosition, newPosition, reason)
            Log.d(TAG, "onPositionDiscontinuity: $oldPosition, $newPosition, $reason")
            updateState()
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            Log.d(TAG, "onMediaItemTransition: $mediaItem, $reason")
            updateState()
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            super.onTimelineChanged(timeline, reason)
            Log.d(TAG, "onTimelineChanged: $timeline, $reason")
            updateState()
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            super.onShuffleModeEnabledChanged(shuffleModeEnabled)
            Log.d(TAG, "onShuffleModeEnabledChanged: $shuffleModeEnabled")
            updateState()
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
            Log.d(TAG, "onRepeatModeChanged: $repeatMode")
            updateState()
        }
    }

    // endregion
}

// region mapper

fun WorkItem.toBundle(): Bundle = Bundle().also {
    it.putLong("id", this.id)
    it.putString("sourceId", this.sourceId)
    it.putString("sourceType", this.sourceType)
    it.putString("coverUrl", this.coverUrl)
}

fun Bundle.toWorkItem(): WorkItem? {
    val id = this.getLong("id")
    val sourceId = this.getString("sourceId") ?: return null
    val sourceType = this.getString("sourceType") ?: return null
    val coverUrl = this.getString("coverUrl") ?: return null
    return WorkItem(id, sourceId, sourceType, coverUrl)
}

fun TrackItem.Audio.toMediaItem(): MediaItem {
    val bundle = Bundle().also {
        it.putString("hash", this.hash)
        it.putString("title", this.title)
        it.putBundle("work", this.work?.toBundle())
        it.putString("workTitle", this.workTitle)
        it.putString("streamUrl", this.streamUrl)
        it.putString("streamLowQualityUrl", this.streamLowQualityUrl)
        it.putString("downloadUrl", this.downloadUrl)
        it.putLong("fileSize", this.fileSize)
        it.putLong("duration", this.duration)
    }

    return MediaItem.Builder()
        .setMediaId(this.hash)
        .setUri(this.streamUrl)
        .setMediaMetadata(MediaMetadata.Builder().setExtras(bundle).build())
        .setTag(this)
        .build()
}

fun MediaItem.toAudio(): TrackItem.Audio? {
    val extra = this.mediaMetadata.extras ?: return null
    return TrackItem.Audio(
        hash = extra.getString("hash") ?: return null,
        title = extra.getString("title") ?: return null,
        work = extra.getBundle("work")?.toWorkItem() ?: return null,
        workTitle = extra.getString("workTitle") ?: return null,
        streamUrl = extra.getString("streamUrl") ?: return null,
        streamLowQualityUrl = extra.getString("streamLowQualityUrl") ?: return null,
        downloadUrl = extra.getString("downloadUrl") ?: return null,
        fileSize = extra.getLong("fileSize"),
        duration = extra.getLong("duration")
    )
}

// endregion

// region delegate function

private operator fun <T> MutableStateFlow<T>.setValue(
    thisObj: Any?,
    property: KProperty<*>,
    value: T
) {
    this.value = value
}

private operator fun <T> MutableStateFlow<T>.getValue(thisObj: Any?, property: KProperty<*>): T =
    this.value

// endregion