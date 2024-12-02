package com.example.pago.data.network

import com.example.pago.data.network.entities.PersonRemote
import com.example.pago.data.network.entities.PostRemote
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PagoApi {
    @GET("users")
    fun getPersons(): Single<List<PersonRemote>>

    @GET("users/{userId}/posts")
    fun getPosts(@Path("userId") userId: Int): Single<List<PostRemote>>
}