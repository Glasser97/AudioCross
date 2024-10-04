package com.grayson.audiocross.domain.exceptions

class RequestNotOkException(private val error: CommonError) : RuntimeException() {
    override fun toString(): String {
        return super.toString() + " -> " + error
    }
}