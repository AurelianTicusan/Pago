package com.example.pago.data.framework

import com.example.pago.ACTIVE_STATUS
import com.example.pago.PersonRemoteDataSource
import com.example.pago.data.mapper.toPerson
import com.example.pago.data.mapper.toPost
import com.example.pago.domain.PersonsRepository
import com.example.pago.domain.model.Person
import com.example.pago.domain.model.Post
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PersonsRepositoryImpl @Inject constructor(
    private val remoteDataSource: PersonRemoteDataSource
) : PersonsRepository {

    override fun getPersons(): Single<List<Person>> {
        return remoteDataSource.getPersons()
            .map { it.map { person -> person.toPerson() } }
    }

    override fun getActivePersons(): Single<List<Person>> {
        return remoteDataSource.getPersons()
            .map { it.map { person -> person.toPerson() } }
            .toObservable()
            .flatMapIterable { it }
            .filter { it.status == ACTIVE_STATUS }
            .toList()
    }

    override fun getPosts(userId: Int): Single<List<Post>> {
        return remoteDataSource.getPosts(userId)
            .map { it.map { post -> post.toPost() } }
    }
}