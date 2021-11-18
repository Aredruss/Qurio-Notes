package com.aredruss.qurio.di

import android.app.Application
import androidx.room.Room
import com.aredruss.qurio.domain.database.NoteDao
import com.aredruss.qurio.domain.database.QurioDatabase
import com.aredruss.qurio.model.Note
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(application: Application): QurioDatabase {
        return Room.databaseBuilder(application, QurioDatabase::class.java, Note.TABLE_NAME)
            .build()
    }

    fun provideNoteDao(database: QurioDatabase): NoteDao {
        return  database.noteDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideNoteDao(get()) }
}