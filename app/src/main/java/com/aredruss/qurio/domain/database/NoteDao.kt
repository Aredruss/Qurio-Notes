package com.aredruss.qurio.domain.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aredruss.qurio.models.Note

interface NoteDao {
    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM ${Note.TABLE_NAME}")
    suspend fun getNotes(): List<Note>

    @Query("SELECT * FROM ${Note.TABLE_NAME} WHERE id = :id")
    suspend fun getNote(id: Int): List<Note>
}