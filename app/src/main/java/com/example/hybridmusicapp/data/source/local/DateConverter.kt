package com.example.hybridmusicapp.data.source.local

import androidx.room.TypeConverter
import java.util.Date

object DateConverter {

    @TypeConverter
    fun toDate(dateTime: Long): Date {
        return Date(dateTime)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}