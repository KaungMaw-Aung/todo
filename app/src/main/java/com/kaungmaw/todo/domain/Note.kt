package com.kaungmaw.todo.domain

import com.google.firebase.Timestamp


data class Note(
    val id: String,
    val title: String,
    val note: String,
    val createdAt: Timestamp
)
