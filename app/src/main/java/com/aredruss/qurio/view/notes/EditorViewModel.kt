package com.aredruss.qurio.view.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.qurio.domain.NoteRepository
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.utils.Event
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
        val resultId =
            noteRepo.insertNote(
                Note(
                    id = 0,
                    name = title.ifEmpty { "Untitled" },
                    text = text,
                    date = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.time,
                )
            )
        val note = noteRepo.getNoteById(resultId)
        noteState.update {
            it.copy(
                isNewNote = false,
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
    val isNewNote: Boolean = false,
    val isChanged: Boolean = false,
    val isDeleted: Boolean = false,
    val currentNote: Note? = null,
    val error: Event<String>? = null
)