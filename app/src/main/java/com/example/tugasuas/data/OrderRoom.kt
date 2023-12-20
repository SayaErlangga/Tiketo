package com.example.tugasuas.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_table")
data class OrderRoom(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    @ColumnInfo(name = "stasiun_asal")
    val stasiunAsal: String,

    @ColumnInfo(name = "stasiun_tujuan")
    val stasiunTujuan: String,

    @ColumnInfo(name = "harga")
    val harga: String,

    @ColumnInfo(name = "user")
    val user: String,

    @ColumnInfo(name = "list_fitur")
    val listFitur: List<String> = mutableListOf()
)
