package com.example.pago.data.network.entities

import com.google.gson.annotations.SerializedName

data class PostRemote(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("body") var body: String? = null
)
