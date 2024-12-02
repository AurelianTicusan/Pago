package com.example.pago.presentation.mapper

import com.example.pago.domain.model.Person
import com.example.pago.presentation.state.PersonItem

fun Person.toPersonItem(): PersonItem = PersonItem(
    id = id,
    name = name,
    gender = gender,
    status = status,
    email = email
)