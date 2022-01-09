package com.aredruss.qurio.repo

import com.aredruss.qurio.domain.database.NoteDao
import com.aredruss.qurio.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.util.*

class NoteRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val noteDao: NoteDao
) {

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    suspend fun getNoteById(id: Long) = noteDao.getNoteById(id.toInt())

    fun getNotes(date: Date?): Flow<List<Note>> =
        if (date != null) {
            noteDao.getNotesByDate(date)
        } else {
            noteDao.getNotes()
        }.flowOn(ioDispatcher)
}
