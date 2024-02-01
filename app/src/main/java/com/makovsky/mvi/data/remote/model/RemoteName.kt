package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteName (
    @SerializedName("name")
    val name: String?,
)