package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.Type

data class RemoteType (
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: RemoteName,
)

fun RemoteType.toType(): Type {
    return Type(
        slot,
        type.name ?: ""
    )
}