package com.aredruss.qurio.view.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.qurio.domain.NoteRepo
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.utils.Event
import com.aredruss.qurio.view.utils.update
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NoteViewModel(
    private val noteRepo: NoteRepo
) : ViewModel() {

    val notesStateLD = MutableLiveData(
        NotesState(
            loading = Event(true)
        )
    )

    init {
        loadNotes()
    }

    fun loadNotes() = viewModelScope.launch {
        notesStateLD.update { it.copy(loading = Event(true)) }
        noteRepo.getNotes()
            .catch { error ->
                notesStateLD.update {
                    it.copy(
                        error = Event("$error"),
                        loading = Event(false)
                    )
                }
            }
            .collect { notes ->
                notesStateLD.update {
                    it.copy(
                        userNotes = notes,
                        isEmpty = notes.isEmpty(),
                        loading = Event(false)
                    )
                }
            }
    }
}

data class NotesState(
    val loading: Event<Boolean>? = null,
    val error: Event<String>? = null,
    val isEmpty: Boolean = false,
    val userNotes: List<Note> = emptyList()
)
