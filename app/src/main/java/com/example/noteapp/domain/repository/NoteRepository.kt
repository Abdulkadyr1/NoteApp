package com.example.noteapp.domain.repository

import com.example.noteapp.domain.models.Note

interface NoteRepository {
    fun getAllNotes(): List<Note>
    fun addNote(note: Note)
    fun deleteNote(note: Note)
    fun updateNote(note: Note)

}