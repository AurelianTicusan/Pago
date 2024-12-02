package com.example.pago.domain.usecase

import com.example.pago.domain.PersonsRepository
import com.example.pago.domain.model.Person
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetPersonsUseCase @Inject constructor(private val repository: PersonsRepository) {
    fun execute(): Single<List<Person>> {
        return repository.getPersons().map {
            return@map it
        }
    }
}