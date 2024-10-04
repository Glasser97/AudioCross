package com.grayson.audiocross.data.albumlist.model.common

data class CommonError(
    var error: String
) {
    override fun toString(): String {
        return "CommonError(error='$error')"
    }
}
