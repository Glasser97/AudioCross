package com.grayson.audiocross.domain.database

import com.grayson.audiocross.domain.login.model.User
import kotlinx.coroutines.flow.Flow

interface IUserInfoHelper {

    suspend fun insert(userInfo: User)

    suspend fun delete(userInfo: User)

    fun get(): Flow<User?>

    fun close()
}