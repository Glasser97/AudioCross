package com.grayson.audiocross

import android.app.Application
import com.grayson.audiocross.data.albumlist.repository.AlbumListRepository
import com.grayson.audiocross.domain.albumlist.repository.IAlbumListRepository
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
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
            single<IAlbumListRepository> {
                AlbumListRepository()
            }
            factory {
                FetchAlbumListUseCase()
            }
        }

        startKoin {
            androidLogger()
            androidContext(this@AudioCrossApplication)
            modules(appModule)
        }
    }
}
