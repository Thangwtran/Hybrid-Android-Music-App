package com.example.hybridmusicapp.data.source.local

import androidx.room.TypeConverters
import java.util.Date

object DateConverter {

    @TypeConverters
    fun toDate(dateTime:Long):Date{
        return Date(dateTime)
    }

    @TypeConverters
    fun fromDate(date: Date):Long{
        return date.time
    }
}