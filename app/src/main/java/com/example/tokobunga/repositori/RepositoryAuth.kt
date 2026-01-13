package com.example.tokobunga.repositori

import com.example.tokobunga.apiservice.ServiceApiAuth
import com.example.tokobunga.modeldata.Admin
import retrofit2.Response

interface RepositoryAuth {
    suspend fun login(email: String, password: String): Response<Admin>
}

class JaringanRepositoryAuth(
    private val serviceApiAuth: ServiceApiAuth
) : RepositoryAuth {

    override suspend fun login(
        email: String,
        password: String
    ): Response<Admin> {
        val body = mapOf(
            "email" to email,
            "password" to password
        )
        return serviceApiAuth.loginAdmin(body)
    }
}
