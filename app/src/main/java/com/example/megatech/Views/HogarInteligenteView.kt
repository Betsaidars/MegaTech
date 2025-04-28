package com.example.megatech.Views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.megatech.Components.ProductoItem
import com.example.megatech.R
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.MainViewModel
import com.example.megatech.ViewModels.MainViewModelFactory

@Composable
fun HogarInteligenteView(navController: NavController, sessionManager: SessionManager) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(sessionManager))
    val hogarInteligente by mainViewModel.hogarInteligente.collectAsState()

    var expandedMenu by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Log.d("TELES_VIEW", "Number of hogar inteligente: ${hogarInteligente.size}")

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra superior modificada
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Menú Hamburguesa
            Box(contentAlignment = Alignment.CenterStart) {
                IconButton(onClick = { expandedMenu = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_24),
                        contentDescription = "Menu",
                    )
                }
                DropdownMenu(
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Perfil") },
                        onClick = {
                            expandedMenu = false
                            navController.navigate("profile")
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        onClick = {
                            expandedMenu = false
                            sessionManager.logout()
                            navController.navigate("login") {
                                popUpTo("Main") { inclusive = true }
                            }
                        }
                    )

                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f) // El buscador ocupa el espacio restante
            ) {
                // Logo Clicable
                IconButton(onClick = { navController.navigate("Main") }) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo de la empresa",
                        modifier = Modifier.size(30.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                // Buscador
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                    placeholder = { Text("Buscar productos") },
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                )
            }

            // Iconos finales
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = { /* TODO: Navegar al carrito */ }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                }
                IconButton(onClick = { navController.navigate("listaDeDeseos") }) {
                    Icon(Icons.Filled.FavoriteBorder, contentDescription = "Lista de deseos")
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Hogar Inteligente",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (hogarInteligente.isEmpty()) {
                Text("No hay televisores disponibles.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(hogarInteligente) { hogarInteligente ->
                        ProductoItem(producto = hogarInteligente, navController = navController)
                    }
                }
            }
        }
    }
}