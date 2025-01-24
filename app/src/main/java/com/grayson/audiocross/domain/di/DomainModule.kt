package com.grayson.audiocross.domain.di

import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumInfoUseCase
import com.grayson.audiocross.domain.albuminfo.usecase.FetchAlbumTracksUseCase
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import com.grayson.audiocross.domain.login.usecase.LoginInfoCheckUseCase
import com.grayson.audiocross.domain.login.usecase.LoginUseCase
import com.grayson.audiocross.domain.search.usecase.SearchAlbumListUseCase
import org.koin.dsl.module

val domainModule = module {
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
}