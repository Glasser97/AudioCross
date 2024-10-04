package com.grayson.audiocross.data.albumlist.model.work

import com.google.gson.annotations.SerializedName
import com.grayson.audiocross.data.albumlist.model.common.Attribute
import java.io.Serial

data class Tag(
    @SerializedName("i18n")
    val i18n: Map<String, Attribute>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
) {
    override fun toString(): String {
        return "Tag(i18n=$i18n, id=$id, name='$name')"
    }
}
