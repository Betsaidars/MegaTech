package com.example.megatech.Views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.megatech.Data.Pedido
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.PedidoViewModel
import com.example.megatech.ViewModels.PedidoViewModelFactory

@SuppressLint("UnrememberedGetBackStackEntry")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPedidosView(navController: NavController, sessionManager: SessionManager, pedidoViewModel: PedidoViewModel) {

    val listaPedidos by pedidoViewModel.listaPedidos.collectAsState()
    Log.d("MisPedidosView", "Tamaño de la lista de pedidos al recomponer: ${listaPedidos.size}")
    Log.d("MisPedidosView", "Lista de pedidos observada: $listaPedidos")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Pedidos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (listaPedidos.isEmpty()) {
                    Text("No hay pedidos realizados aún.")
                } else {
                    LazyColumn {
                        items(listaPedidos) { pedido ->
                            PedidoItem(pedido = pedido)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun PedidoItem(pedido: Pedido) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pedido #${pedido.id}", style = MaterialTheme.typography.titleMedium)
            Text("Fecha: ${pedido.fecha}", style = MaterialTheme.typography.bodyMedium)
            Text("Nombre: ${pedido.nombreUsuario}", style = MaterialTheme.typography.bodyMedium)
            Text("Dirección: ${pedido.direccionEnvio}", style = MaterialTheme.typography.bodyMedium)
            Text("Items:", style = MaterialTheme.typography.bodyMedium)
            Column(modifier = Modifier.padding(start = 8.dp)) {
                pedido.items.forEach { item ->
                    Text("- $item", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("Total: ${String.format("%.2f", pedido.total)}€", style = MaterialTheme.typography.titleSmall)
        }
    }
}