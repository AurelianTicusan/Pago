package com.example.pago.data.mapper

import com.example.pago.data.network.entities.PersonRemote
import com.example.pago.domain.model.Person

fun PersonRemote.toPerson(): Person = Person(
    id = id ?: -1,
    name = name.orEmpty(),
    gender = gender.orEmpty(),
    status = status.orEmpty(),
    email = email.orEmpty()
)