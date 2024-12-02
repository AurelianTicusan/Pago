package com.example.pago.domain.usecase

import com.example.pago.domain.PersonsRepository
import com.example.pago.domain.model.Post
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetPostsByUserIdUseCase @Inject constructor(private val repository: PersonsRepository) {
    fun execute(userId: Int): Single<List<Post>> {
        return repository.getPosts(userId)
    }
}