package com.example.megatech.Views

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.megatech.Data.LanguageSelector
import com.example.megatech.R
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.BannerCincuentaViewModel
import com.example.megatech.ViewModels.BannerCincuentaViewModelFactory

@OptIn(UnstableApi::class)
@Composable
fun BannerCincuentaView(navController: NavController){
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val bannerCincuentaViewModel: BannerCincuentaViewModel = viewModel(factory = BannerCincuentaViewModelFactory(sessionManager))
    val discountedFiftyItems by bannerCincuentaViewModel.discountedItemsCincuenta.collectAsState()
    val isLoading by bannerCincuentaViewModel.isLoadingDiscountedItemsCincuenta.collectAsState()
    val error by bannerCincuentaViewModel.errorLoadingDiscountedItemsCincuenta.collectAsState()

    var expandedMenu by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        bannerCincuentaViewModel.getDiscountedItemsCincuenta()
    }

    Column(modifier = Modifier.fillMaxSize()) {
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
                        text = { Text(stringResource(R.string.menu_profile)) },
                        onClick = {
                            expandedMenu = false
                            navController.navigate("profile")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.menu_my_orders)) }, // Nuevo botón "Mis Pedidos"
                        onClick = {
                            expandedMenu = false
                            navController.navigate("misPedidos") // Navega a la ruta de "misPedidos"
                        }
                    )

                    DropdownMenuItem(
                        text = { Text((stringResource(R.string.menu_logout))) },
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
                IconButton(onClick = { navController.navigate("Main")}) {
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
                    placeholder = { Text(stringResource(R.string.search_placeholder)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                )
            }

            // Iconos finales
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = { navController.navigate("carritoDeCompra") }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                }
                IconButton(onClick = { navController.navigate("listaDeDeseos") }) { // Navegar a la lista de deseos
                    Icon(Icons.Filled.FavoriteBorder, contentDescription = "Lista de deseos")
                }
                LanguageSelector()
            }
        }


    if (isLoading) {
        Text("Cargando items con descuento...")
    } else if (error != null) {
        Text("Error al cargar los items con descuento: $error")
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(discountedFiftyItems) { itemWithFiftyDiscount ->
                    Log.d(
                        "BannerTreintaView",
                        "Image URL: ${itemWithFiftyDiscount.imageUrlDiscointFifty}"
                    )
                    Column(
                        modifier = Modifier
                            .clickable {
                                navController.navigate("itemDetail/${itemWithFiftyDiscount.idDiscointFifty}")
                            }
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = itemWithFiftyDiscount.imageUrlDiscointFifty ?: ""
                            ),
                            contentDescription = itemWithFiftyDiscount.nameDiscointFifty,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = itemWithFiftyDiscount.nameDiscointFifty,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "${itemWithFiftyDiscount.normalPriceDiscointFifty} €",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Text(
                            text = "${itemWithFiftyDiscount.priceWithDiscointFifty} €",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}
}