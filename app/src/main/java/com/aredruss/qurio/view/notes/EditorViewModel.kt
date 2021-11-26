package com.aredruss.qurio.view.notes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.qurio.domain.NoteRepository
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.utils.Event
import com.aredruss.qurio.view.utils.update
import kotlinx.coroutines.launch
import java.util.*

class EditorViewModel(
    private val noteRepo: NoteRepository,
    private val note: Note?
) : ViewModel() {

    val noteState: MutableLiveData<NoteState> = MutableLiveData()

    init {
        noteState.update {
            it.copy(
                isChanged = false,
                currentNote = note,
                error = null
            )
        }
    }

    fun createNote(title: String, text: String) = viewModelScope.launch {
        val resultId =
            noteRepo.insertNote(
                Note(
                    id = 0,
                    name = title.ifEmpty { "Untitled" },
                    text = text,
                    date = Calendar.getInstance().time,
                )
            )
        val note = noteRepo.getNoteById(resultId)
        noteState.update {
            it.copy(
                currentNote = note
            )
        }
    }

    fun updateNote(title: String, text: String) = viewModelScope.launch {
        noteState.value?.currentNote?.let {
            noteRepo.updateNote(it.copy(name = title, text = text))
        }
    }

    fun deleteNote() = viewModelScope.launch {
        noteState.value?.currentNote?.let {
            noteRepo.deleteNote(it)
        }

    }
}

data class NoteState(
    val isChanged: Boolean = false,
    val isDeleted: Boolean = false,
    val currentNote: Note? = null,
    val error: Event<String>? = null
)