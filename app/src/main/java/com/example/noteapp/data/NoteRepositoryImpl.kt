package com.example.noteapp.data

import com.example.noteapp.domain.models.Note
import com.example.noteapp.domain.repository.NoteRepository

class NoteRepositoryImpl(
)  : NoteRepository{

    private val itemList = mutableListOf<Note>()

    override fun getAllNotes(): List<Note> {
        return itemList.toList()
    }

    override fun addNote(note: Note) {
        itemList.add(note)
    }

    override fun deleteNote(note: Note) {
        itemList.remove(note)
    }

    override fun updateNote(note: Note) {
        itemList.forEachIndexed { index, it ->
            if (it.id == note.id){
                itemList[index] = note
                return
            }
        }
    }
}