package com.example.tokobunga.uicontroller.route


import com.example.tokobunga.R

object DestinasiDetailBunga : DestinasiNavigasi {
    override val route = "detail_bunga"
    override val titleRes = R.string.detail_bunga

    const val ID_BUNGA = "idBunga"
    val routeWithArg = "$route/{$ID_BUNGA}"
}
