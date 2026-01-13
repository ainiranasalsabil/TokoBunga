package com.example.tokobunga.uicontroller.route

import com.example.tokobunga.R

object DestinasiEditBunga : DestinasiNavigasi {
    override val route = "edit_bunga"
    override val titleRes = R.string.edit_bunga

    const val ID_BUNGA = "idBunga"
    val routeWithArg = "$route/{$ID_BUNGA}"
}
