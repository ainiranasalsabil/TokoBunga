package com.example.tokobunga.view.bunga


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tokobunga.view.components.FloristTopAppBar
import com.example.tokobunga.viewmodel.bunga.DetailBungaViewModel
import com.example.tokobunga.viewmodel.bunga.StatusDetailBunga
import com.example.tokobunga.viewmodel.provide.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBungaScreen(
    navigateBack: () -> Unit,
    navigateToEdit: (Int) -> Unit,
    navigateToStok: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailBungaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    Scaffold(
        topBar = {
            FloristTopAppBar(
                title = "Detail Bunga",
                canNavigateBack = true,
                onNavigateBack = navigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->

        when (val state = viewModel.statusDetail) {

            is StatusDetailBunga.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is StatusDetailBunga.Error -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Gagal memuat detail bunga")
                }
            }

            is StatusDetailBunga.Success -> {
                val bunga = state.bunga

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // ================= FOTO =================
                    AsyncImage(
                        model = bunga.foto_bunga,
                        contentDescription = bunga.nama_bunga,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )

                    // ================= INFO =================
                    Text(
                        text = bunga.nama_bunga,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text("Kategori : ${bunga.kategori}")
                    Text("Harga    : Rp ${bunga.harga}")
                    Text("Stok     : ${bunga.stok}")

                    Divider()

                    // ================= ACTION BUTTONS =================
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Button(
                            onClick = { navigateToEdit(bunga.id_bunga) },
                            enabled = state.canEdit,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Edit")
                        }

                        OutlinedButton(
                            onClick = { navigateToStok(bunga.id_bunga) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Inventory, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Update Stok")
                        }
                    }

                    OutlinedButton(
                        onClick = { viewModel.hapusBunga(navigateBack) },
                        enabled = state.canDelete,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Hapus Bunga")
                    }
                }
            }
        }
    }
}
