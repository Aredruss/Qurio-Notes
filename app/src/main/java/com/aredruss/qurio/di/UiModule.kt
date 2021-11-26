package com.aredruss.qurio.di

import com.aredruss.qurio.model.Note
import com.aredruss.qurio.view.home.HomeViewModel
import com.aredruss.qurio.view.notes.EditorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { HomeViewModel(noteRepo = get()) }
    viewModel { (note: Note?) -> EditorViewModel(noteRepo = get(), note) }
}