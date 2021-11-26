package com.aredruss.qurio.di

import androidx.room.Room
import com.aredruss.qurio.domain.database.QurioDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { Room.databaseBuilder(get(), QurioDatabase::class.java, "qurio_db").build() }
    single { get<QurioDatabase>().noteDao() }
}
