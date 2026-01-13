package com.example.tokobunga.uicontroller.route

import com.example.tokobunga.R

object DestinasiStok : DestinasiNavigasi {
    override val route = "stok"
    override val titleRes = R.string.stok_title

    const val ID_BUNGA = "idBunga"
    val routeWithArg = "$route/{$ID_BUNGA}"
}
