package com.aredruss.qurio.helpers

import android.text.format.DateUtils
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.*

object DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}