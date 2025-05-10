// FormularioCompraView.kt

package com.example.megatech.Views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.megatech.Model.ItemsModel
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.CarritoDeCompraViewModel
import com.example.megatech.ViewModels.ListaDeDeseosViewModel
import com.example.megatech.ViewModels.PedidoViewModel
import com.example.megatech.ViewModels.PedidoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCompraView(navController: NavController, carritoDeCompraViewModel: CarritoDeCompraViewModel, sessionManager: SessionManager,  pedidoViewModel: PedidoViewModel) {
    val carritoListItems by carritoDeCompraViewModel.itemCarrito.collectAsState()
    val totalPrecioSeleccionado by carritoDeCompraViewModel.totalPrecioSeleccionado.collectAsState(initial = 0.0)
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Información de Envío") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver al carrito")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total: ${String.format("%.2f", totalPrecioSeleccionado)}€",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(
                    onClick = {
                        pedidoViewModel.guardarNuevoPedido(
                            nombreUsuario = nombre,
                            direccionEnvio = direccion,
                            items = carritoListItems.mapNotNull { it.id }, // Obtén los IDs de los items
                            total = totalPrecioSeleccionado
                        )
                        Log.d("FormularioCompra", "Llamada a guardarNuevoPedido realizada")
                        navController.navigate("compraRealizada") {
                            popUpTo("carritoDeCompra") { inclusive = true }
                        }
                    },
                    enabled = nombre.isNotBlank() && direccion.isNotBlank() && carritoListItems.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pagar")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(carritoListItems, key = { it.id ?: "" }) { item -> // Añade una key para optimización
                    FormularioItemRow(
                        item = item,
                        onRemoveItem = { itemToRemove ->
                            carritoDeCompraViewModel.removeItemFromCarritolist(itemToRemove)
                        }
                    )
                    Divider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun FormularioItemRow(item: ItemsModel, onRemoveItem: (ItemsModel) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween // Para alinear el botón al final
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            item.imageUrl.firstOrNull()?.let { url ->
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 8.dp)
                )
            }
            Column {
                Text(text = item.name ?: "", style = MaterialTheme.typography.bodyMedium)
                Text(text = "$${item.price ?: ""}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
        IconButton(onClick = { onRemoveItem(item) }) {
            Icon(Icons.Filled.Delete, contentDescription = "Borrar item")
        }
    }
}