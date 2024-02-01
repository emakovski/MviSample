package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteStat (
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("stat")
    val stat: RemoteName,
)

fun RemoteStat.toStat(): Stat {
    return Stat(
        baseStat,
        stat.name ?: ""
    )
}