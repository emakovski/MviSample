package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.Description

data class RemoteDescription (
    @SerializedName("flavor_text")
    val flavorText: String?,
    @SerializedName("language")
    val language: RemoteLanguage?,
)

fun RemoteDescription.toDescription(): Description = Description(
    if (language?.name == "en") flavorText?.replace("\n", " ") else null
)