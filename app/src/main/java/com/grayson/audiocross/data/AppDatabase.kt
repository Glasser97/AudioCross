package com.grayson.audiocross.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.grayson.audiocross.data.login.dao.BaseContentProvider
import com.grayson.audiocross.data.login.dao.UserInfo
import com.grayson.audiocross.data.login.dao.UserInfoDao

@Database(entities = [UserInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserInfoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private fun getDatabase() = INSTANCE ?: synchronized(AppDatabase::class.java) {
            INSTANCE ?: buildDatabase().also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context = BaseContentProvider.context()): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).build()
        }

        @JvmStatic
        fun getUserDao(): UserInfoDao {
            return getDatabase().userDao()
        }

        @JvmStatic
        fun closeDB() {
            getDatabase().close()
        }

    }

    override fun close() {
        super.close()
        //数据库关闭后把instance置空
        INSTANCE = null
    }

}