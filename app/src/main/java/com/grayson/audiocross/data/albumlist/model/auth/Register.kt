package com.grayson.audiocross.data.albumlist.model.auth

data class Register(
    val token: String
) {
    override fun toString(): String {
        return "Register(token='$token')"
    }
}
