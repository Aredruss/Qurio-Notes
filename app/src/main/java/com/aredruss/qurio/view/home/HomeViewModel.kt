package com.aredruss.qurio.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aredruss.qurio.helpers.Event
import com.aredruss.qurio.helpers.update
import com.aredruss.qurio.model.Note
import com.aredruss.qurio.repo.NoteRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val noteRepo: NoteRepository
) : ViewModel() {

    val notesListStateLD = MutableLiveData(
        NoteListState(
            loading = true
        )
    )

    init {
        loadNotes()
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        noteRepo.deleteNote(note)
    }

    fun loadNotes(date: Date? = null) = viewModelScope.launch {
        notesListStateLD.update { it.copy(loading = true) }
        noteRepo.getNotes(date)
            .catch { error ->
                notesListStateLD.update {
                    it.copy(
                        error = Event("$error"),
                        loading = false
                    )
                }
            }
            .collect { notes ->
                notesListStateLD.update {
                    it.copy(
                        userNotes = notes.sortedBy { it.date }.reversed()
                            .groupBy { note -> note.date },
                        isEmpty = notes.isEmpty(),
                        isFiltered = date != null,
                        loading = false
                    )
                }
            }
    }
}

data class NoteListState(
    val loading: Boolean = false,
    val error: Event<String>? = null,
    val isEmpty: Boolean = false,
    val isFiltered: Boolean = false,
    val userNotes: Map<Date, List<Note>> = emptyMap()
)
