package com.aredruss.qurio.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.qurio.domain.NoteRepository
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.utils.Event
import com.aredruss.qurio.view.utils.update
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val noteRepo: NoteRepository
) : ViewModel() {

    val notesListStateLD = MutableLiveData(
        NoteListState(
            loading = Event(true)
        )
    )

    init {
        loadNotes()
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        noteRepo.deleteNote(note)
    }

    private fun loadNotes() = viewModelScope.launch {
        notesListStateLD.update { it.copy(loading = Event(true)) }
        noteRepo.getNotes()
            .catch { error ->
                notesListStateLD.update {
                    it.copy(
                        error = Event("$error"),
                        loading = Event(false)
                    )
                }
            }
            .collect { notes ->
                notesListStateLD.update {
                    it.copy(
                        userNotes = notes,
                        isEmpty = notes.isEmpty(),
                        loading = Event(false)
                    )
                }
            }
    }
}

data class NoteListState(
    val loading: Event<Boolean>? = null,
    val error: Event<String>? = null,
    val isEmpty: Boolean = false,
    val userNotes: List<Note> = emptyList()
)
