package com.grayson.audiocross.domain.exceptions

data class CommonError(
    var error: String
) {
    override fun toString(): String {
        return "CommonError(error='$error')"
    }
}