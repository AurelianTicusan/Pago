package com.example.pago

import com.example.pago.data.network.PagoApi
import javax.inject.Inject

class PersonRemoteDataSource @Inject constructor(private val pagoApi: PagoApi) {

    fun getPersons() = pagoApi.getPersons()
    fun getPosts(userId: Int) = pagoApi.getPosts(userId)

}