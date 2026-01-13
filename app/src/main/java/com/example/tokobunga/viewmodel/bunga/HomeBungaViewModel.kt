package com.example.tokobunga.viewmodel.bunga

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokobunga.modeldata.Bunga
import com.example.tokobunga.repositori.RepositoryBunga
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface StatusHomeBunga {
    object Loading : StatusHomeBunga
    data class Success(val list: List<Bunga>) : StatusHomeBunga
    object Error : StatusHomeBunga
}

class HomeBungaViewModel(
    private val repositoryBunga: RepositoryBunga
) : ViewModel() {

    // list asli dari server (tidak difilter)
    private var allBunga: List<Bunga> = emptyList()

    // state yang ditampilkan ke UI
    var statusHome: StatusHomeBunga by mutableStateOf(StatusHomeBunga.Loading)
        private set

    // query search
    var searchQuery by mutableStateOf("")
        private set

    init {
        loadBunga()
    }

    fun loadBunga() {
        viewModelScope.launch {
            statusHome = StatusHomeBunga.Loading
            statusHome = try {
                allBunga = repositoryBunga.getListBunga()
                StatusHomeBunga.Success(allBunga)
            } catch (e: IOException) {
                StatusHomeBunga.Error
            } catch (e: HttpException) {
                StatusHomeBunga.Error
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
        val filtered = allBunga.filter {
            it.nama_bunga.contains(query, ignoreCase = true)
        }
        statusHome = StatusHomeBunga.Success(filtered)
    }
}
