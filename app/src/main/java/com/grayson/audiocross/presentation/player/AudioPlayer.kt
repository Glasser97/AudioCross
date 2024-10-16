package com.grayson.audiocross.presentation.player

import android.util.Log
import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.player.IAudioPlayer
import com.grayson.audiocross.domain.player.PlaybackSpeed
import com.grayson.audiocross.domain.player.PlayerState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import kotlin.reflect.KProperty

class AudioPlayer(
    private val mainDispatcher: CoroutineDispatcher
) : IAudioPlayer {

    // region constant

    companion object {
        private const val TAG = "AudioPlayer"
    }

    // endregion

    // region field

    private val _playerState = MutableStateFlow(PlayerState())
    private val _currentAudio = MutableStateFlow<TrackItem.Audio?>(null)
    private val queue = MutableStateFlow<List<TrackItem.Audio>>(emptyList())
    private val isPlaying = MutableStateFlow(false)
    private val timeElapsed = MutableStateFlow(0L)
    private val _playerSpeed = MutableStateFlow(PlaybackSpeed.DefaultSpeed)
    private val coroutineScope = CoroutineScope(mainDispatcher)

    override val playerState: StateFlow<PlayerState> = _playerState
    override var currentAudio: TrackItem.Audio? by _currentAudio
    override val playbackSpeed: PlaybackSpeed = _playerSpeed.value

    private var timerJob: Job? = null

    // endregion

    // region init

    init {
        coroutineScope.launch {
            combine(
                _currentAudio,
                queue,
                isPlaying,
                timeElapsed,
                _playerSpeed
            ) { currentAudio, queue, isPlaying, timeElapsed, playerSpeed ->
                PlayerState(
                    currentAudio = currentAudio,
                    playQueue = queue,
                    isPlaying = isPlaying,
                    timeElapsed = timeElapsed,
                    playbackSpeed = playerSpeed
                )
            }.catch {
                throw it
            }.collect {
                _playerState.update { it }
            }
        }
    }

    // endregion

    // region public

    override fun addToQueue(audio: TrackItem.Audio) {
        queue.update {
            it + audio
        }
    }

    override fun removeAllFromQueue() {
        queue.value = emptyList()
    }

    override fun play() {
        if (isPlaying.value) {
            Log.i(TAG, "play: already playing")
            return
        }

        val audio = currentAudio ?: return

        isPlaying.value = true
        timerJob = coroutineScope.launch {
            while (isActive && timeElapsed.value <= audio.duration) {
                delay(playbackSpeed.milliseconds)
                timeElapsed.update { it + playbackSpeed.milliseconds }
            }
            isPlaying.value = false
            timeElapsed.value = 0L

            if (hasNext()) {
                next()
            }
        }
    }

    override fun play(audio: TrackItem.Audio) {
        play(listOf(audio))
    }

    override fun play(audios: List<TrackItem.Audio>) {
        if (isPlaying.value) {
            pause()
        }


        // Keep the current playing audio in the queue
        val playingAudio = currentAudio
        var previousList: List<TrackItem.Audio> = emptyList()
        queue.update { queue ->
            audios.map { audio ->
                if (queue.contains(audio)) {
                    val mutableList = queue.toMutableList()
                    mutableList.remove(audio)
                    previousList = mutableList
                } else {
                    previousList = queue
                }
            }
            if (playingAudio != null) {
                audios + listOf(playingAudio) + previousList
            } else {
                audios + previousList
            }
        }

        next()
    }

    override fun pause() {
        isPlaying.value = false
        timerJob?.cancel()
        timerJob = null
    }

    override fun stop() {
        isPlaying.value = false
        timeElapsed.value = 0L

        timerJob?.cancel()
        timerJob = null
    }

    override fun next() {
        val q = queue.value
        if (q.isEmpty()) {
            return
        }
        timeElapsed.value = 0
        val nextAudio = q.first()
        currentAudio = nextAudio
        queue.value = q - nextAudio
        play()
    }

    override fun previous() {
        timeElapsed.value = 0
        isPlaying.value = false
        timerJob?.cancel()
        timerJob = null
    }

    override fun changePlaybackSpeed(speed: PlaybackSpeed) {
        _playerSpeed.value = speed
    }

    override fun advanceBy(duration: Long) {
        val currentAudio = _currentAudio.value?.duration ?: return
        timeElapsed.update {
            (it + duration).coerceAtMost(currentAudio)
        }
    }

    override fun rewindBy(duration: Long) {
        timeElapsed.update {
            (it + duration).coerceAtLeast(0L)
        }
    }

    override fun onSeekingStarted() {
        pause()
    }

    override fun onSeekingFinished(duration: Long) {
        val currentAudioDuration = _currentAudio.value?.duration ?: return
        timeElapsed.update {
            duration.coerceIn(0, currentAudioDuration)
        }
        play()
    }

    // endregion

    // region private

    private fun hasNext(): Boolean {
        return queue.value.isNotEmpty()
    }

    // endregion
}

// Used to enable property delegation
private operator fun <T> MutableStateFlow<T>.setValue(
    thisObj: Any?,
    property: KProperty<*>,
    value: T
) {
    this.value = value
}

private operator fun <T> MutableStateFlow<T>.getValue(thisObj: Any?, property: KProperty<*>): T =
    this.value