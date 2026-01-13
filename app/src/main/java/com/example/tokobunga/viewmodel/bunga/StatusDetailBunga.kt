package com.example.tokobunga.viewmodel.bunga

import com.example.tokobunga.modeldata.Bunga

sealed interface StatusDetailBunga {

    /** Saat data sedang dimuat */
    object Loading : StatusDetailBunga

    /** Jika gagal load data */
    object Error : StatusDetailBunga

    /** Jika data berhasil dimuat */
    data class Success(
        val bunga: Bunga,
        val canEdit: Boolean,
        val canDelete: Boolean
    ) : StatusDetailBunga
}
