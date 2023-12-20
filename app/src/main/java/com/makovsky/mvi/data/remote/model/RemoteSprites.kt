package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.Sprites

data class RemoteSprites (
    @SerializedName("other")
    val other: RemoteOtherSprites,
    )

fun RemoteSprites.toSprites(): Sprites = Sprites(other.toOtherSprites())