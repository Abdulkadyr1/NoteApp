package com.example.noteapp.preference

import com.example.noteapp.domain.models.Note

data class UiNoteState(
    val noteList: List<Note> = emptyList(),
    val titleInput: String = "",
    val contentInput: String = "",
    val isUpdating: Long? = null,
    val btnText: String = "Добавить",
)
