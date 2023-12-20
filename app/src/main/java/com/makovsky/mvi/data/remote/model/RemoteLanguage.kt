package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteLanguage (
    @SerializedName("name")
    val name: String?,
)