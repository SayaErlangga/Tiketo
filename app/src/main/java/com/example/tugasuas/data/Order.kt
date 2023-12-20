package com.example.tugasuas.data

data class Order(
    var id: String = "",
    var stasiunAsal: String = "",
    var stasiunTujuan: String = "",
    var harga: String = "",
    var tanggal: String = "",
    var kelas: String = "",
    var user: String? = null,
    var listFitur: List<String> = mutableListOf()
)
