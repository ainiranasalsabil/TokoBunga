package com.example.tokobunga.modeldata

import android.net.Uri

data class DetailBunga(
    val id_bunga: Int = 0,
    val nama_bunga: String = "",
    val kategori: String = "",
    val harga: String = "",
    val stok: String = "",
    val fotoUri: Uri? = null
)
