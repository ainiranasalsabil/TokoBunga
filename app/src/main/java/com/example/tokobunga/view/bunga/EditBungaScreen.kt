package com.example.tokobunga.view.bunga

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tokobunga.R
import com.example.tokobunga.view.components.DropdownKategori
import com.example.tokobunga.view.components.FloristTopAppBar
import com.example.tokobunga.viewmodel.bunga.EditBungaViewModel
import com.example.tokobunga.viewmodel.provide.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBungaScreen(
    idBunga: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditBungaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    val errorMessage = viewModel.errorMessage

    // ================= DIALOG ERROR (dari ViewModel) =================
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Gagal Memperbarui")
                }
            },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("Mengerti")
                }
            }
        )
    }

    // ================= LOAD DATA =================
    LaunchedEffect(idBunga) {
        viewModel.loadDataById(idBunga)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FloristTopAppBar(
                title = stringResource(R.string.edit_bunga),
                canNavigateBack = true,
                onNavigateBack = navigateBack,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ================= FORM DATA =================
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        OutlinedTextField(
                            value = viewModel.nama,
                            onValueChange = viewModel::onNamaChange,
                            label = { Text(stringResource(R.string.nama_bunga)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        DropdownKategori(
                            selected = viewModel.kategori,
                            onSelected = viewModel::onKategoriChange
                        )

                        OutlinedTextField(
                            value = viewModel.harga,
                            onValueChange = viewModel::onHargaChange,
                            label = { Text(stringResource(R.string.harga)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            prefix = { Text("Rp ") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ================= UPDATE =================
                Button(
                    onClick = {
                        isLoading = true
                        viewModel.updateBunga(
                            onSuccess = {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "Data bunga berhasil diperbarui",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navigateBack()
                            },
                            onError = { pesan ->
                                isLoading = false
                                Toast.makeText(context, pesan, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(160.dp)
                        .height(46.dp),
                    shape = RoundedCornerShape(50),
                    enabled = !isLoading
                ) {
                    Text(stringResource(R.string.btn_update))
                }
            }

            // ================= LOADING =================
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
