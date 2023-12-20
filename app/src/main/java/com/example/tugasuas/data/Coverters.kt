package com.example.tugasuas.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun toStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }
}
