package com.grayson.audiocross.data.albumlist.model.auth

data class User(
    val name: String,
    val group: String
) {
    override fun toString(): String {
        return "User(name='$name', group='$group')"
    }
}
