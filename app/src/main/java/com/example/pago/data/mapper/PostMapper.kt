package com.example.pago.data.mapper

import com.example.pago.data.network.entities.PostRemote
import com.example.pago.domain.model.Post

fun PostRemote.toPost(): Post = Post(
    id = id ?: -1,
    userId = userId ?: -1,
    title = title.orEmpty(),
    body = body.orEmpty()
)