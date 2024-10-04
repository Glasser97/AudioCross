package com.grayson.audiocross.data.albumlist.model.auth

data class AuthMe(
    val user: User,
    val auth: Boolean,
    val reg: Boolean
) {
    override fun toString(): String {
        return "AuthMe(user=$user, auth=$auth, reg=$reg)"
    }
}
