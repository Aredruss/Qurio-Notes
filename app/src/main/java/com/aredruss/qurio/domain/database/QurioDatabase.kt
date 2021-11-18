package com.aredruss.qurio.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aredruss.qurio.helpers.DateConverter
import com.aredruss.qurio.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class QurioDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
