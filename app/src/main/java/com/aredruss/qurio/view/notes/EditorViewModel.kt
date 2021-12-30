package com.aredruss.qurio.view.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.qurio.domain.NoteRepository
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.utils.Event
import com.aredruss.qurio.view.utils.getClearDate
import com.aredruss.qurio.view.utils.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class EditorViewModel(
    private val noteRepo: NoteRepository,
    note: Note?
) : ViewModel() {

    val noteState: MutableLiveData<NoteState> = MutableLiveData(
        NoteState(
            isNewNote = note == null,
            isChanged = false,
            currentNote = note,
            error = null
        )
    )

    fun createNote(title: String, text: String) = viewModelScope.launch {
        val currentNote = noteState.value?.currentNote
        val newNote = Note(
            id = 0,
            title = title.ifEmpty { "Untitled" },
            text = text,
            date = getClearDate()
        )
        if (newNote.text != currentNote?.text || newNote.title != currentNote.title) {
            val resultId = noteRepo.insertNote(newNote)
            val note = noteRepo.getNoteById(resultId)
            noteState.update { it.copy(isNewNote = false, currentNote = note) }
        }
    }

    fun updateNote(title: String, text: String) = viewModelScope.launch {
        val currentNote = noteState.value?.currentNote
        if (text != currentNote?.text || title != currentNote.title) {
            noteState.value?.currentNote?.let {
                val updatedNote = it.copy(
                    title = title.ifEmpty { "Untitled" },
                    text = text,
                    date = getClearDate()
                )
                noteRepo.updateNote(updatedNote)
            }
        }
    }

    fun deleteNote() = viewModelScope.launch {
        noteState.value?.currentNote?.let {
            noteRepo.deleteNote(it)
        }
    }

    private fun getClearDate(): Date = Calendar.getInstance().getClearDate()

}

data class NoteState(
    val isNewNote: Boolean = false,
    val isChanged: Boolean = false,
    val isDeleted: Boolean = false,
    val currentNote: Note? = null,
    val noteToShare: Event<Note>? = null,
    val error: Event<String>? = null
)