package com.example.tokobunga.viewmodel.bunga

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokobunga.repositori.RepositoryBunga
import kotlinx.coroutines.launch

class DetailBungaViewModel(
    private val repositoryBunga: RepositoryBunga
) : ViewModel() {

    var statusDetail: StatusDetailBunga by mutableStateOf(StatusDetailBunga.Loading)
        private set

    fun loadDetail(id: Int) {
        statusDetail = StatusDetailBunga.Loading
        viewModelScope.launch {
            try {
                val bunga = repositoryBunga.getDetailBunga(id)

                statusDetail = StatusDetailBunga.Success(
                    bunga = bunga,
                    canEdit = true,
                    canDelete = true
                )
            } catch (e: Exception) {
                statusDetail = StatusDetailBunga.Error
            }
        }
    }

    fun hapusBunga(onSuccess: () -> Unit) {
        val current = statusDetail
        if (current is StatusDetailBunga.Success) {
            viewModelScope.launch {
                repositoryBunga.deleteBunga(current.bunga.id_bunga)
                onSuccess()
            }
        }
    }
}
