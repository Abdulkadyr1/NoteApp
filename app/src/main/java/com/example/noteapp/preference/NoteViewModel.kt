package com.example.noteapp.preference

import android.text.BoringLayout
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.noteapp.data.NoteRepositoryImpl
import com.example.noteapp.domain.models.Note
import com.example.noteapp.domain.repository.NoteRepository

class NoteViewModel(
    private val noteRepo: NoteRepository,
): ViewModel() {
    var uiState by mutableStateOf(UiNoteState())
        private set


    init {
        loadNotes()
    }

    fun loadNotes(){
        val temp = noteRepo.getAllNotes()
        uiState = uiState.copy(noteList = temp)
    }


    fun onTitleChange(newValue: String){
        uiState = uiState.copy(titleInput = newValue)
    }

    fun onContentChange(newValue: String){
        uiState = uiState.copy(contentInput = newValue)
    }

    fun deleteNote(note: Note){
        noteRepo.deleteNote(note)
        loadNotes()
    }

    fun onAddNote() {
        if (uiState.titleInput.isBlank() && uiState.contentInput.isBlank()) return

        val newNote = Note(
            id = System.currentTimeMillis(),
            title = uiState.titleInput,
            content = uiState.contentInput,
            createdAt = System.currentTimeMillis(),
        )

        noteRepo.addNote(newNote)

        val updateNotes = noteRepo.getAllNotes()
        uiState = uiState.copy(
            noteList = updateNotes,
            titleInput = "",
            contentInput = "",
        )

    }

}