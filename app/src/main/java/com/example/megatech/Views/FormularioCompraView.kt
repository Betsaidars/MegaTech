package com.example.megatech.Views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.megatech.Model.ItemsModel
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.CarritoDeCompraViewModel
import com.example.megatech.ViewModels.PedidoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCompraView(
    navController: NavController,
    carritoDeCompraViewModel: CarritoDeCompraViewModel,
    sessionManager: SessionManager,
    pedidoViewModel: PedidoViewModel
) {
    val carritoListItems by carritoDeCompraViewModel.itemCarrito.collectAsState()
    val totalPrecioSeleccionado by carritoDeCompraViewModel.totalPrecioSeleccionado.collectAsState(initial = 0.0)
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    val paymentMethods = listOf("Tarjeta de Crédito", "Bizum", "PayPal")
    var expanded by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf("Método de Pago") }
    var codigoDescuento by remember { mutableStateOf("") }
    val descuentoAplicado = remember { mutableStateOf(0.0) }
    val descuentoAplicadoCodigo = remember { mutableStateOf("") }
    val mainScrollState = rememberScrollState() // Estado para el scroll principal

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
                val precioConDescuento = totalPrecioSeleccionado - descuentoAplicado.value
                Text(
                    text = "Total: ${String.format("%.2f", precioConDescuento.coerceAtLeast(0.0))}€",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (descuentoAplicado.value > 0.0) {
                    Text(
                        text = "Descuento aplicado: ${String.format("%.2f", descuentoAplicado.value)}€ (${descuentoAplicadoCodigo.value})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Green,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Button(
                    onClick = {
                        pedidoViewModel.guardarNuevoPedido(
                            nombreUsuario = nombre,
                            direccionEnvio = direccion,
                            items = carritoListItems.mapNotNull { it.id },
                            total = precioConDescuento.coerceAtLeast(0.0),
                            metodoPago = selectedPaymentMethod,
                            codigoDescuento = descuentoAplicadoCodigo.value.ifBlank { null }
                        )
                        carritoDeCompraViewModel.borrarCarrito()
                        Log.d(
                            "FormularioCompra",
                            "Llamada a guardarNuevoPedido realizada con método de pago: $selectedPaymentMethod y código: ${descuentoAplicadoCodigo.value}"
                        )
                        navController.navigate("compraRealizada") {
                            popUpTo("carritoDeCompra") { inclusive = true }
                        }
                    },
                    enabled = nombre.isNotBlank() && direccion.isNotBlank() && carritoListItems.isNotEmpty() && selectedPaymentMethod.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp)

                ) {
                    Text("Pagar")
                }
            }
        }
    ) { paddingValues ->
        Column( // Volvemos a usar Column como contenedor principal
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(mainScrollState), // Aplicamos verticalScroll al Column principal
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Column( // Usamos un Column normal para la lista de ítems
                modifier = Modifier.fillMaxWidth()
            ) {
                carritoListItems.forEach { item -> // Iteramos sobre la lista directamente
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
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Desplegable para el método de pago
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedPaymentMethod,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Método de Pago") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    placeholder = {
                        if (selectedPaymentMethod == "Método de Pago") {
                            Text(
                                text = "Método de Pago",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    paymentMethods.forEach { paymentMethod ->
                        DropdownMenuItem(
                            text = { Text(text = paymentMethod) },
                            onClick = {
                                selectedPaymentMethod = paymentMethod
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fila para el código de descuento y el botón
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = codigoDescuento,
                    onValueChange = { codigoDescuento = it },
                    label = { Text("Código Descuento") },
                    modifier = Modifier.weight(0.7f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (descuentoAplicadoCodigo.value != codigoDescuento) {
                            val descuento = when (codigoDescuento) {
                                "betsaida" -> totalPrecioSeleccionado * 0.3
                                "megatech" -> 40.0
                                "proyecto" -> totalPrecioSeleccionado * 0.15
                                else -> 0.0
                            }.coerceAtMost(totalPrecioSeleccionado)
                            descuentoAplicado.value = descuento
                            descuentoAplicadoCodigo.value = codigoDescuento
                        } else {
                            descuentoAplicado.value = 0.0
                            descuentoAplicadoCodigo.value = ""
                            codigoDescuento = ""
                        }
                    },
                    modifier = Modifier.weight(0.3f),
                    shape = RoundedCornerShape(4.dp)

                ) {
                    Text(if (descuentoAplicadoCodigo.value == codigoDescuento && descuentoAplicadoCodigo.value.isNotBlank()) "Quitar" else "Aplicar")
                }
            }
            if (descuentoAplicadoCodigo.value.isNotBlank() && descuentoAplicado.value > 0.0) {
                Text(
                    text = "Descuento aplicado: ${String.format("%.2f", descuentoAplicado.value)}€",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Green,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(16.dp)) // Añade un espacio al final del contenido
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
        horizontalArrangement = Arrangement.SpaceBetween
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