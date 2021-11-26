package com.aredruss.qurio.di

import com.aredruss.qurio.domain.NoteRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val domainModule = module {
    single { NoteRepository(ioDispatcher = Dispatchers.IO, noteDao = get()) }
}