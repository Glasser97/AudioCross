package com.grayson.audiocross.data.login.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.grayson.audiocross.domain.login.model.User

@Entity(primaryKeys = ["username"])
data class UserInfo(
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "token")
    val token: String
) {
    companion object {
        fun from(user: User): UserInfo {
            return UserInfo(user.username, user.token)
        }

        fun to(userInfo: UserInfo): User {
            return User(userInfo.username, userInfo.token)
        }
    }
}