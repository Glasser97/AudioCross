package com.grayson.audiocross.data.albumlist.model.circle

data class Circle(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return "Circle(id=$id, name='$name')"
    }
}
