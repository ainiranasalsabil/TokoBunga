package com.example.tokobunga.repositori

import com.example.tokobunga.apiservice.ServiceApiAuth
import com.example.tokobunga.apiservice.ServiceApiBunga
import com.example.tokobunga.apiservice.ServiceApiStok
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface ContainerApp {
    val repositoryAuth: RepositoryAuth
    val repositoryBunga: RepositoryBunga
    val repositoryStok: RepositoryStok
}

class DefaultContainerApp : ContainerApp {

    // ===============================
    // BASE URL SERVER (XAMPP)
    // ===============================
    private val baseUrl = "http://10.0.2.2/FLORIST_API/"
    // ===============================
    // LOGGING (DEBUG API)
    // ===============================
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // ===============================
    // JSON CONFIG
    // ===============================
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    // ===============================
    // RETROFIT
    // ===============================
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .client(client)
        .build()

    // ===============================
    // API SERVICE
    // ===============================
    private val serviceApiAuth: ServiceApiAuth by lazy {
        retrofit.create(ServiceApiAuth::class.java)
    }

    private val serviceApiBunga: ServiceApiBunga by lazy {
        retrofit.create(ServiceApiBunga::class.java)
    }

    private val serviceApiStok: ServiceApiStok by lazy {
        retrofit.create(ServiceApiStok::class.java)
    }

    // ===============================
    // REPOSITORY
    // ===============================
    override val repositoryAuth: RepositoryAuth by lazy {
        JaringanRepositoryAuth(serviceApiAuth)
    }

    override val repositoryBunga: RepositoryBunga by lazy {
        JaringanRepositoryBunga(serviceApiBunga)
    }

    override val repositoryStok: RepositoryStok by lazy {
        JaringanRepositoryStok(serviceApiStok)
    }
}
