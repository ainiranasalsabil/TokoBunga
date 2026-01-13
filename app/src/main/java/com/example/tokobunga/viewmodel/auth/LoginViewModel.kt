package com.example.tokobunga.viewmodel.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokobunga.modeldata.Admin
import com.example.tokobunga.repositori.RepositoryAuth
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface StatusLogin {
    object Idle : StatusLogin
    object Loading : StatusLogin
    data class Success(val admin: Admin) : StatusLogin
    data class Error(val message: String) : StatusLogin
}

class LoginViewModel(
    private val repositoryAuth: RepositoryAuth
) : ViewModel() {

    var statusLogin: StatusLogin by mutableStateOf(StatusLogin.Idle)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            statusLogin = StatusLogin.Loading
            try {
                val response = repositoryAuth.login(email, password)
                if (response.isSuccessful && response.body() != null) {
                    statusLogin = StatusLogin.Success(response.body()!!)
                } else {
                    statusLogin = StatusLogin.Error("Email atau password salah")
                }
            } catch (e: IOException) {
                statusLogin = StatusLogin.Error("Koneksi gagal")
            } catch (e: HttpException) {
                statusLogin = StatusLogin.Error("Server error")
            }
        }
    }

    fun resetStatus() {
        statusLogin = StatusLogin.Idle
    }
}