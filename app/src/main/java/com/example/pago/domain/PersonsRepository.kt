package com.example.pago.domain

import com.example.pago.domain.model.Person
import com.example.pago.domain.model.Post
import io.reactivex.rxjava3.core.Single

interface PersonsRepository {
    fun getPersons(): Single<List<Person>>
    fun getActivePersons(): Single<List<Person>>
    fun getPosts(userId: Int): Single<List<Post>>
}