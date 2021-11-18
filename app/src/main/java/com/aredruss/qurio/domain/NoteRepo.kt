package com.aredruss.qurio.domain

import com.aredruss.qurio.domain.database.NoteDao
import com.aredruss.qurio.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NoteRepo(
    private val ioDispatcher: CoroutineDispatcher,
    private val noteDao: NoteDao
) {

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    fun getNotes() = flow {
        emit(noteDao.getNotes())
    }.flowOn(ioDispatcher)
}
