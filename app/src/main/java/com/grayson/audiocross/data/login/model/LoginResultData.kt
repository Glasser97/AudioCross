package com.grayson.audiocross.data.login.model

import com.google.gson.annotations.SerializedName

data class LoginResultData(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val userInfo: LoginUserInfo
)

data class LoginUserInfo(
    @SerializedName("loggedIn")
    val loggedIn: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("group")
    val group: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("recommenderUuid")
    val uuid: String
)
