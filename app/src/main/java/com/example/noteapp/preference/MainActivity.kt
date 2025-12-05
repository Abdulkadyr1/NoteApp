package com.example.noteapp.preference

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
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

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            Text("Заметки", fontSize = 40.sp, fontWeight = FontWeight.Bold)
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = state.noteList,
                    key = { it.id }
                ) { note ->
                    Card(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        NoteItem(
                            note = note,
                            deleteNote = { viewModel.deleteNote(note = note) },
                            onEdit = { viewModel.startEdit(note = note) }
                        )
                    }
                }
            }
        }
        androidx.compose.material3.FloatingActionButton(
            modifier = Modifier
                .align (Alignment.BottomEnd)
                .padding(bottom = 35.dp, end = 15.dp),
            onClick = {
                viewModel.showAddNote()
            }) {
            Text("+")
        }

        if (state.isAddNote){
            showAddNoteUi(state, viewModel)
        }

    }
}
@Composable
fun showAddNoteUi(state: UiNoteState,
                  viewModel: NoteViewModel){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(value = state.titleInput, onValueChange = { viewModel.onTitleChange(it) }, Modifier.padding(bottom = 20.dp))
            TextField(value = state.contentInput, onValueChange = { viewModel.onContentChange(it) })
            Button(
//                modifier = Modifier
                onClick = {
                    viewModel.onAddNote()
                }) {
                Text("Сохранить")
            }
        }
    }
}

@Composable
fun NoteItem(note: Note,
             deleteNote: (Note) -> Unit,
             onEdit: (Note) -> Unit,){
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
            }
        ) {
            Text("Редактировать заметку")
        }
    }
}

