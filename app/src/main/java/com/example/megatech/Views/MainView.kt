package com.example.megatech.Views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.MainViewModel
import com.example.megatech.ViewModels.MainViewModelFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.megatech.R
import androidx.compose.material.*
import androidx.compose.runtime.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainView(navController: NavController, sessionManager: SessionManager) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(sessionManager))

    val banners = mainViewModel.banners.collectAsState().value
    val items = mainViewModel.items.collectAsState().value
    val pagerState = rememberPagerState(pageCount = { banners.size })
    val isLoadingItems = mainViewModel.isLoadingItems.collectAsState().value
    val errorLoadingItems = mainViewModel.errorLoadingItems.collectAsState().value

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mainViewModel.getAllBanner()
        mainViewModel.getAllItems()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Mantenemos SpaceBetween para el logo
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de la empresa",
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )

        Box(
            contentAlignment = Alignment.TopEnd
        ) { // Usamos un Box y alineamos su contenido al TopEnd
            IconButton(onClick = { expanded = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_menu_24),
                    contentDescription = "Menu",
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxHeight()
                    .padding(top = 5.dp)
            ) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    navController.navigate("profile")
                }) {
                    Text("Perfil")
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    sessionManager.logout()
                    navController.navigate("login") {
                        popUpTo("Main") { inclusive = true }
                    }
                }) {
                    Text("Cerrar sesión")
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(top = 100.dp)
        ) { page ->
            Image(
                painter = rememberImagePainter(banners[page].imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        navController.navigate("Discount")

                        println("Banner ${page + 1 } clicked")
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
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { navController.navigate("televicion") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.televicion), contentDescription = "Icono 1")
            }
            IconButton(onClick = { navController.navigate("consola") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.consola), contentDescription = "Icono 2")
            }
            IconButton(onClick = { navController.navigate("ordenador") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.ordenador), contentDescription = "Icono 3")
            }
            IconButton(onClick = { navController.navigate("home") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.home), contentDescription = "Icono 4")
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { navController.navigate("camara") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.camara), contentDescription = "Icono 5")
            }
            IconButton(onClick = { navController.navigate("sonido") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.sonido), contentDescription = "Icono 6")
            }
            IconButton(onClick = { navController.navigate("phone") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.phone), contentDescription = "Icono 7")
            }
            IconButton(onClick = { navController.navigate("reloj") },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.reloj), contentDescription = "Icono 8")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Más vendidos",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

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
                            text = "$${item.price}",
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
