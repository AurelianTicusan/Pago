package com.example.pago.presentation.mapper

import com.example.pago.domain.model.Post
import com.example.pago.presentation.state.PostItem

fun Post.toPostItem(): PostItem = PostItem(
    id = id,
    userId = userId,
    title = title,
    body = body
)