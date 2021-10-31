package com.aredruss.qurio.domain.database

import androidx.room.Database
import com.aredruss.qurio.models.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class QurioDatabase {
    abstract fun noteDao(): NoteDao
}