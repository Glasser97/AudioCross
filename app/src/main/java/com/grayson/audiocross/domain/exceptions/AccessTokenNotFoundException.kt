package com.grayson.audiocross.domain.exceptions

class AccessTokenNotFoundException : RuntimeException() {
    override fun toString(): String {
        return super.toString() + " -> " + " You should login at first or set accessToken manually"
    }
}