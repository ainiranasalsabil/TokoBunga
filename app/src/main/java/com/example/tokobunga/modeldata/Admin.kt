package com.example.tokobunga.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class Admin(
    val id_admin: String,
    val nama_lengkap: String,
    val email: String
)
