package com.grayson.audiocross.presentation.di

import com.grayson.audiocross.presentation.albuminfo.viewmodel.AlbumInfoViewModel
import com.grayson.audiocross.presentation.albumlist.viewmodel.AlbumListViewModel
import com.grayson.audiocross.presentation.login.viewmodel.LoginViewModel
import com.grayson.audiocross.presentation.navigator.viewmodel.AudioCrossViewModel
import com.grayson.audiocross.presentation.player.viewmodel.PlayerViewModel
import com.grayson.audiocross.presentation.search.viewmodel.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

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

    viewModel {
        SearchViewModel()
    }

    viewModel {
        PlayerViewModel()
    }

    viewModel {
        LoginViewModel()
    }
}