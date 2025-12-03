package com.example.noteapp.preference

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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

    fun onUpdateChange(){
        uiState = uiState.copy(btnText = "Сохранить")
    }

    fun deleteNote(note: Note){
        noteRepo.deleteNote(note)
        loadNotes()
    }

    fun startEdit(note: Note){
        uiState = uiState.copy(
            isUpdating = note.id,
            titleInput = note.title,
            contentInput = note.content,
        )
    }

    fun onAddNote() {
        if (uiState.titleInput.isBlank() && uiState.contentInput.isBlank()) return

        val isUpdating = uiState.isUpdating

        if (isUpdating == null){
            val newNote = Note(
                id = System.currentTimeMillis(),
                title = uiState.titleInput,
                content = uiState.contentInput,
                createdAt = System.currentTimeMillis(),
            )
            noteRepo.addNote(newNote)
        } else{
            val updateNote = Note(
                id = isUpdating,
                title = uiState.titleInput,
                content = uiState.contentInput,
                createdAt = System.currentTimeMillis(),
            )
            noteRepo.updateNote(updateNote)
        }


        val updateNotes = noteRepo.getAllNotes()
        uiState = uiState.copy(
            noteList = updateNotes,
            titleInput = "",
            contentInput = "",
            isUpdating = null,
            btnText = "Добавить",
        )

    }

}