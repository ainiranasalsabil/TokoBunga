package com.example.tokobunga.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloristTopAppBar(
    title: String,
    modifier: Modifier = Modifier,

    // navigasi
    canNavigateBack: Boolean = false,
    onNavigateBack: () -> Unit = {},

    // aksi di kanan
    showLogout: Boolean = false,
    onLogoutClick: () -> Unit = {},

    showLaporan: Boolean = false,
    onLaporanClick: () -> Unit = {},

    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,

        // ================= LEFT ICON =================
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali"
                    )
                }
            }
        },

        // ================= RIGHT ICONS =================
        actions = {

            if (showLaporan) {
                IconButton(onClick = onLaporanClick) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = "Laporan"
                    )
                }
            }

            if (showLogout) {
                IconButton(onClick = onLogoutClick) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout"
                    )
                }
            }
        }
    )
}
