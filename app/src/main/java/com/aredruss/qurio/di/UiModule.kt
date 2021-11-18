package com.aredruss.qurio.di

import com.aredruss.qurio.view.notes.NoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.lang.reflect.Array.get

val uiModule = module {
    viewModel { NoteViewModel(noteRepo = get()) }
}