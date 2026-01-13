package com.example.tokobunga.view.bunga

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tokobunga.modeldata.Bunga
import com.example.tokobunga.view.components.FloristTopAppBar
import com.example.tokobunga.viewmodel.bunga.HomeBungaViewModel
import com.example.tokobunga.viewmodel.bunga.StatusHomeBunga
import com.example.tokobunga.viewmodel.provide.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBungaScreen(
    onAddClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    onLogout: () -> Unit,
    onLaporanClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeBungaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    Scaffold(
        topBar = {
            FloristTopAppBar(
                title = "Daftar Bunga",
                showLogout = true,
                showLaporan = true,
                onLogoutClick = onLogout,
                onLaporanClick = onLaporanClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Bunga"
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // ================= SEARCH =================
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                label = { Text("Cari bunga") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ================= LIST =================
            when (val state = viewModel.statusHome) {

                is StatusHomeBunga.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is StatusHomeBunga.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Gagal memuat data")
                    }
                }

                is StatusHomeBunga.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.list, key = { it.id_bunga }) { bunga ->
                            ItemBunga(
                                bunga = bunga,
                                onClick = { onItemClick(bunga.id_bunga) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemBunga(
    bunga: Bunga,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = bunga.foto_bunga,
                contentDescription = bunga.nama_bunga,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = bunga.nama_bunga,
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Harga : Rp ${bunga.harga}")
                Text("Stok  : ${bunga.stok}")
            }
        }
    }
}
