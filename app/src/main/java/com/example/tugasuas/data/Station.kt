package com.example.tugasuas.data

data class Station(
    var id: String = "",
    var stasiunAsal: String = "",
    var stasiunTujuan: String = "",
    var harga: String = "",
    var listFitur: List<String> = mutableListOf()
)
