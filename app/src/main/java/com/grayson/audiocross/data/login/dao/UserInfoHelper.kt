package com.grayson.audiocross.data.login.dao

import com.grayson.audiocross.data.AppDatabase
import com.grayson.audiocross.domain.database.IUserInfoHelper
import com.grayson.audiocross.domain.login.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserInfoHelper : IUserInfoHelper {
    override suspend fun insert(userInfo: User) {
        AppDatabase.getUserDao().insert(UserInfo.from(userInfo))
    }

    override suspend fun delete(userInfo: User) {
        AppDatabase.getUserDao().delete(UserInfo.from(userInfo))
    }

    override fun get(): Flow<User?> {
        return AppDatabase.getUserDao().get().map { userInfo ->
            userInfo?.let {
                UserInfo.to(userInfo)
            }
        }
    }

    override fun close() {
        AppDatabase.closeDB()
    }
}