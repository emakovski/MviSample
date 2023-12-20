package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.OtherSprites

data class RemoteOtherSprites (
    @SerializedName("official-artwork")
    val artwork: RemoteArtwork,
)

fun RemoteOtherSprites.toOtherSprites(): OtherSprites = OtherSprites(artwork.toArtwork())