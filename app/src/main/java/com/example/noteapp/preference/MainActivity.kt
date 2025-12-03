package com.example.noteapp.preference

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.data.NoteRepositoryImpl
import com.example.noteapp.domain.models.Note

class MainActivity : ComponentActivity() {

    private val vm: NoteViewModel by lazy {
        val noteRep = NoteRepositoryImpl()
        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NoteViewModel(
                    noteRepo = noteRep
                ) as T
            }
        }
        ViewModelProvider(this, factory)[NoteViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues())
            ) {
                NoteScreen(vm)
            }
        }
    }
}

@Composable
fun NoteScreen(viewModel: NoteViewModel){
    val state = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        TextField(value = state.titleInput, onValueChange = { viewModel.onTitleChange(it) })
        TextField(value = state.contentInput, onValueChange = { viewModel.onContentChange(it) })

        Button(
            modifier = Modifier.padding(bottom = 10.dp, top = 5.dp),
            onClick = {
            viewModel.onAddNote()
        }) {
            Text(text = state.btnText)
        }

        Text("Список заметок:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(
                items = state.noteList,
                key = { it.id }
            ){ note ->
                Card(
                    modifier = Modifier.padding(5.dp)
                ) {
                    NoteItem(note = note,
                        deleteNote = { viewModel.deleteNote(note = note) },
                        onEdit = {viewModel.startEdit(note = note)},
                        onBtn = {viewModel.onUpdateChange()})
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: Note,
             deleteNote: (Note) -> Unit,
             onEdit: (Note) -> Unit,
             onBtn: () -> Unit){
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        Text("Title: ${note.title}", fontSize = 18.sp)
        Text("Content: ${note.content}", fontSize = 18.sp)

        Button(
            onClick = {
                deleteNote(note)
            }
        ) {
            Text("Удалить заметку")
        }

        Button(
            onClick = {
                onEdit(note)
                onBtn()
            }
        ) {
            Text("Редактировать заметку")
        }
    }
}