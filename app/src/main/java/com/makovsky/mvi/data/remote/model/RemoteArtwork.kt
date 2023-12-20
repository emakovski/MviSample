package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.Artwork

data class RemoteArtwork (
    @SerializedName("front_default")
    val frontDefault: String?,
)

fun RemoteArtwork.toArtwork(): Artwork = Artwork(frontDefault)