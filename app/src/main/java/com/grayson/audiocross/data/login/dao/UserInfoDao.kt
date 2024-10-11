package com.grayson.audiocross.data.login.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserInfo): Long

    @Delete
    suspend fun delete(user: UserInfo): Int

    @Query("SELECT * FROM userinfo")
    fun get(): Flow<UserInfo?>

}