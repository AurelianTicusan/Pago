package com.example.pago.data.network.entities

import com.google.gson.annotations.SerializedName

data class PersonRemote(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("status") var status: String? = null
)
