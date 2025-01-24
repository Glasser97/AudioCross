package com.grayson.audiocross

import android.app.Application
import com.grayson.audiocross.data.albuminfo.repository.AlbumInfoRepository
import com.grayson.audiocross.data.albuminfo.repository.AlbumTracksRepository
import com.grayson.audiocross.data.albumlist.repository.AlbumListRepository
import com.grayson.audiocross.data.login.dao.UserInfoHelper
import com.grayson.audiocross.data.login.repository.LoginRepository
import com.grayson.audiocross.data.search.repository.SearchAlbumRepository
import com.grayson.audiocross.domain.albuminfo.repository.IAlbumInfoRepository
import com.grayson.audiocross.domain.albuminfo.repository.IAlbumTracksRepository
import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumInfoUseCase
import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumTracksUseCase
import com.grayson.audiocross.domain.albumlist.repository.IAlbumListRepository
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import com.grayson.audiocross.domain.database.IUserInfoHelper
import com.grayson.audiocross.domain.login.repository.ILoginRepository
import com.grayson.audiocross.domain.login.usecase.LoginInfoCheckUseCase
import com.grayson.audiocross.domain.login.usecase.LoginUseCase
import com.grayson.audiocross.domain.player.IAudioPlayer
import com.grayson.audiocross.domain.search.repository.ISearchAlbumRepository
import com.grayson.audiocross.domain.search.usecase.SearchAlbumListUseCase
import com.grayson.audiocross.presentation.albuminfo.viewmodel.AlbumInfoViewModel
import com.grayson.audiocross.presentation.albumlist.viewmodel.AlbumListViewModel
import com.grayson.audiocross.presentation.navigator.viewmodel.AudioCrossViewModel
import com.grayson.audiocross.presentation.player.ExoAudioPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Application class
 */
class AudioCrossApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            single<ISearchAlbumRepository> {
                SearchAlbumRepository()
            }
            single<IAlbumListRepository> {
                AlbumListRepository()
            }
            single<ILoginRepository> {
                LoginRepository()
            }
            single<IAlbumInfoRepository> {
                AlbumInfoRepository()
            }
            single<IAlbumTracksRepository> {
                AlbumTracksRepository()
            }
            single<IUserInfoHelper> {
                UserInfoHelper()
            }
            single<IAudioPlayer> {
                ExoAudioPlayer(
                    this@AudioCrossApplication.applicationContext
                )
            }
            factory {
                SearchAlbumListUseCase()
            }
            factory {
                FetchAlbumListUseCase()
            }
            factory {
                LoginInfoCheckUseCase()
            }
            factory {
                LoginUseCase()
            }
            factory {
                FetchAlbumInfoUseCase()
            }
            factory {
                FetchAlbumTracksUseCase()
            }

            viewModel { parameters ->
                AlbumInfoViewModel(
                    albumId = parameters.get()
                )
            }

            viewModel {
                AlbumListViewModel()
            }

            viewModel {
                AudioCrossViewModel()
            }


        }

        startKoin {
            androidLogger()
            androidContext(this@AudioCrossApplication)
            modules(appModule)
        }
    }
}
