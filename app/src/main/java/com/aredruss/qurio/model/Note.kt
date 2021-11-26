package com.aredruss.qurio.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = Note.TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "date")
    val date: Date
): Parcelable {
    companion object {
        const val TABLE_NAME = "notes"
    }
}