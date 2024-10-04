package com.grayson.audiocross.data.albumlist.model.auth

data class Login(
    val token: String
) {
    override fun toString(): String {
        return "Login(token='$token')"
    }
}
