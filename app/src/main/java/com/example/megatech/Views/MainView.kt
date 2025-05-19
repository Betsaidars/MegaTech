package com.example.megatech.Views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.megatech.R
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.MainViewModel
import com.example.megatech.ViewModels.MainViewModelFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import com.example.megatech.Data.LanguageSelector


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainView(navController: NavController, sessionManager: SessionManager) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(sessionManager))

    val banners by mainViewModel.banners.collectAsState()
    val items by mainViewModel.items.collectAsState(initial = emptyList())
    val pagerState = rememberPagerState(
        pageCount = { banners.size },
        initialPage = 0 // Añade un valor inicial para la página (por ejemplo, 0)
    )
    val isLoadingItems by mainViewModel.isLoadingItems.collectAsState()
    val errorLoadingItems by mainViewModel.errorLoadingItems.collectAsState()

    var expandedMenu by remember { mutableStateOf(false) }
    val searchText by mainViewModel.searchText.collectAsState() // Observamos el estado del texto de búsqueda

    LaunchedEffect(Unit) {
        mainViewModel.getAllBanner()
        mainViewModel.getAllItems()
    }

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
                        text = { Text(stringResource(R.string.menu_profile)) },
                        onClick = {
                            expandedMenu = false
                            navController.navigate("perfil")
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
                IconButton(onClick = { /* TODO: Implementar acción al hacer clic en el logo */ }) {
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
                    onValueChange = { mainViewModel.onSearchTextChange(it) }, // Llamamos a la función del ViewModel al cambiar el texto
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

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(top = 16.dp) // Reducido el padding superior
        ) { page ->
            Image(
                painter = rememberImagePainter(banners[page].imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (page == 0) { // Primer banner
                            println("Navigating to DiscountTreinta")
                            navController.navigate("DiscountTreinta")
                        } else if (page == 1) { // Segundo banner (índice 1)
                            println("Navigating to DiscountCincuenta")
                            navController.navigate("DiscountCincuenta")
                        }
                        println("Banner ${page + 1} clicked")
                    }
            )
        }

        Row(
            Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(pagerState.pageCount) { index ->
                val color = if (pagerState.currentPage == index) Color.DarkGray else Color.LightGray
                Surface(
                    modifier = Modifier.size(10.dp),
                    shape = CircleShape,
                    color = color
                ) {}
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { navController.navigate("television") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.televicion), contentDescription = "Televisión")
            }
            IconButton(onClick = { navController.navigate("consola") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.consola), contentDescription = "Consolas")
            }
            IconButton(onClick = { navController.navigate("ordenador") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.ordenador), contentDescription = "Ordenadores")
            }
            IconButton(onClick = { navController.navigate("home") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.home), contentDescription = "Hogar")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { navController.navigate("camara") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.camara), contentDescription = "Cámaras")
            }
            IconButton(onClick = { navController.navigate("sonido") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.sonido), contentDescription = "Sonido")
            }
            IconButton(onClick = { navController.navigate("phone") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.phone), contentDescription = "Teléfonos")
            }
            IconButton(onClick = { navController.navigate("reloj") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.reloj), contentDescription = "Relojes")
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Reducido el espacio

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Más vendidos",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(items) { item ->
                    Column(
                        modifier = Modifier
                            .clickable {
                                navController.navigate("itemDetail/${item.id}")
                            }
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = rememberImagePainter(item.imageUrl.firstOrNull() ?: ""),
                            contentDescription = item.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "${item.price} €",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}