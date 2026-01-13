package com.example.tokobunga.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class LogStok(
    val id_log: Int,
    val id_bunga: Int,
    val nama_bunga: String,   // ⬅️ TAMBAHKAN INI
    val jenis: String,        // masuk / keluar
    val jumlah: Int,
    val tanggal: String
)

