package com.example.megatech.Views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.megatech.Components.ProductoItem
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.MainViewModel
import com.example.megatech.ViewModels.MainViewModelFactory

@Composable
fun SonidoView(navController: NavController, sessionManager: SessionManager) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(sessionManager))
    val sonidos by mainViewModel.sonido.collectAsState()

    Log.d("TELES_VIEW", "Number of sonido: ${sonidos.size}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Sonido",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (sonidos.isEmpty()) {
            Text("No hay televisores disponibles.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(sonidos) { sonido ->
                    ProductoItem(producto = sonido, navController = navController)
                }
            }
        }
    }

}