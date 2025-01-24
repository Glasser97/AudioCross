package com.grayson.audiocross.data.di

import com.grayson.audiocross.data.albuminfo.repository.AlbumInfoRepository
import com.grayson.audiocross.data.albuminfo.repository.AlbumTracksRepository
import com.grayson.audiocross.data.albumlist.repository.AlbumListRepository
import com.grayson.audiocross.data.login.dao.UserInfoHelper
import com.grayson.audiocross.data.login.repository.LoginRepository
import com.grayson.audiocross.data.search.repository.SearchAlbumRepository
import com.grayson.audiocross.domain.albuminfo.repository.IAlbumInfoRepository
import com.grayson.audiocross.domain.albuminfo.repository.IAlbumTracksRepository
import com.grayson.audiocross.domain.albumlist.repository.IAlbumListRepository
import com.grayson.audiocross.domain.database.IUserInfoHelper
import com.grayson.audiocross.domain.login.repository.ILoginRepository
import com.grayson.audiocross.domain.search.repository.ISearchAlbumRepository
import org.koin.dsl.module

val dataModule = module {
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
}