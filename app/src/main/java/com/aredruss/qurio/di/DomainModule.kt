package com.aredruss.qurio.di

import com.aredruss.qurio.domain.NoteRepo
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val domainModule = module {
    single { NoteRepo(ioDispatcher = Dispatchers.IO, noteDao = get()) }
}