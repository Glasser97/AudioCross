package com.grayson.audiocross

import android.app.Application
import com.grayson.audiocross.data.di.dataModule
import com.grayson.audiocross.domain.di.domainModule
import com.grayson.audiocross.domain.player.IAudioPlayer
import com.grayson.audiocross.presentation.di.uiModule
import com.grayson.audiocross.presentation.player.ExoAudioPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

/**
 * Application class
 */
class AudioCrossApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            single<IAudioPlayer> {
                ExoAudioPlayer(
                    this@AudioCrossApplication.applicationContext
                )
            }
        }

        startKoin {
            androidLogger()
            androidContext(this@AudioCrossApplication)
            modules(listOf(appModule, dataModule, uiModule, domainModule))
        }
    }
}
