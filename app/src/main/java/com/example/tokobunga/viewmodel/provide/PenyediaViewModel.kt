package com.example.tokobunga.viewmodel.provide

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tokobunga.repositori.FloristApplication
import com.example.tokobunga.viewmodel.auth.LoginViewModel
import com.example.tokobunga.viewmodel.bunga.*
import com.example.tokobunga.viewmodel.laporan.LaporanStokViewModel
import com.example.tokobunga.viewmodel.stok.StokViewModel

fun CreationExtras.floristApplication(): FloristApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FloristApplication)

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer { LoginViewModel(floristApplication().containerApp.repositoryAuth) }
        initializer { HomeBungaViewModel(floristApplication().containerApp.repositoryBunga) }
        initializer { EntryBungaViewModel(floristApplication().containerApp.repositoryBunga) }
        initializer { EditBungaViewModel(floristApplication().containerApp.repositoryBunga) }
        initializer { DetailBungaViewModel(floristApplication().containerApp.repositoryBunga) }
        initializer { StokViewModel(floristApplication().containerApp.repositoryStok) }
        initializer { LaporanStokViewModel(floristApplication().containerApp.repositoryStok) }
    }
}

