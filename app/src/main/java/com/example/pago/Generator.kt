package com.example.pago

import androidx.compose.runtime.mutableStateListOf
import com.example.pago.presentation.state.PersonItem
import io.reactivex.rxjava3.core.Single

fun generatePersons(): Single<List<PersonItem>> {
    val list = mutableStateListOf<PersonItem>().apply {
        repeat(12) {
            add(
                PersonItem(
                    it,
                    "Person Number $it",
                    "person_number_${it}@gmail.com",
                    "",
                    "active"
                )
            )
        }
    }
    return Single.just(list)
}
