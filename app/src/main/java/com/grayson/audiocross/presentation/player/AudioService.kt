package com.grayson.audiocross.presentation.player

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class AudioService : MediaSessionService(), Player.Listener {

    // region const

    companion object {
        const val TAG = "AudioService"
        const val VIEW_MEDIA_SCREEN_ACTION = "MEDIA_SCREEN_ACTION"
    }

    // endregion

    // region field

    private lateinit var mediaSession: MediaSession

    // endregion

    // region override

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) {
            // Stop the service if the player is not playing.
            // Continue playing in the background otherwise.
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession.run {
            player.release()
            release()
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return if (!this::mediaSession.isInitialized) mediaSession else null
    }

    // endregion

    // region Player.Listener


    // endregion

    // region private

    private fun initMediaSession() {
        val player = ExoPlayer.Builder(this).build().apply {
            this.addListener(this@AudioService)
        }
        val callback = object : MediaSession.Callback {

        }
        mediaSession =
            MediaSession.Builder(this, player)
                .setCallback(callback)
                .setSessionActivity(buildPendingIntent())
                .build()
    }

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent(this, Class.forName("com.grayson.audiocross.MainActivity"))
        intent.action = VIEW_MEDIA_SCREEN_ACTION
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
    }

//    private fun buildCustomCallback(): MediaSession.Callback {
//        val customCommands = buildCommandButtons()
//        return object : MediaSession.Callback {
//            override fun onConnect(
//                session: MediaSession,
//                controller: MediaSession.ControllerInfo
//            ): MediaSession.ConnectionResult {
//                val connectionResult = super.onConnect(session, controller)
//                val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()
//                    .add(SessionCommand(Commands.SET_SLEEP_TIMER, Bundle.EMPTY))
//                    .add(SessionCommand(Commands.CANCEL_SLEEP_TIMER, Bundle.EMPTY))
//                customCommands.forEach { commandButton ->
//                    // Add custom command to available session commands.
//                    commandButton.sessionCommand?.let { availableSessionCommands.add(it) }
//                }
//                return MediaSession.ConnectionResult.accept(
//                    availableSessionCommands.build(),
//                    connectionResult.availablePlayerCommands
//                )
//            }
//
//            override fun onCustomCommand(
//                session: MediaSession,
//                controller: MediaSession.ControllerInfo,
//                customCommand: SessionCommand,
//                args: Bundle
//            ): ListenableFuture<SessionResult> {
//                if (Commands.JUMP_FORWARD == customCommand.customAction) {
//                    seekForward()
//                }
//                if (Commands.JUMP_BACKWARD == customCommand.customAction) {
//                    seekBackward()
//                }
//                if (Commands.SET_SLEEP_TIMER == customCommand.customAction) {
//                    val minutes = args.getInt("MINUTES", 0)
//                    val finishLastSong = args.getBoolean("FINISH_LAST_SONG", false)
//                    sleepTimerManager.schedule(minutes, finishLastSong)
//                }
//                if (Commands.CANCEL_SLEEP_TIMER == customCommand.customAction) {
//                    sleepTimerManager.deleteTimer()
//                }
//                return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
//            }
//        }
//
//    }


    // endregion

}