package com.example.noteapp.domain.models

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Long,
)
