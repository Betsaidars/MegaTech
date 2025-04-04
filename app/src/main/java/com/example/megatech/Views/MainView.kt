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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart // Ejemplo de icono
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource // Si usas iconos de recursos
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.megatech.R // Si tus iconos están en recursos

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainView(navController: NavController, sessionManager: SessionManager) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(sessionManager))

    val banners = mainViewModel.banners.collectAsState().value
    val items = mainViewModel.items.collectAsState().value
    val pagerState = rememberPagerState(pageCount = { banners.size })
    val isLoadingItems = mainViewModel.isLoadingItems.collectAsState().value
    val errorLoadingItems = mainViewModel.errorLoadingItems.collectAsState().value

    LaunchedEffect(Unit) {
        mainViewModel.getAllBanner()
        mainViewModel.getAllItems()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) { page ->
            Image(
                painter = rememberImagePainter(banners[page].imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        //Aqui pondría la naveganción a la otra página

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
            IconButton(onClick = { /* Navegar a la ventana del icono 1 */ },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.televicion), contentDescription = "Icono 1")
            }
            IconButton(onClick = { /* Navegar a la ventana del icono 2 */ },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.consola), contentDescription = "Icono 2")
            }
            IconButton(onClick = { /* Navegar a la ventana del icono 3 */ },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.ordenador), contentDescription = "Icono 3")
            }
            IconButton(onClick = { /* Navegar a la ventana del icono 4 */ },
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
            IconButton(onClick = { /* Navegar a la ventana del icono 5 */ },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.camara), contentDescription = "Icono 5")
            }
            IconButton(onClick = { /* Navegar a la ventana del icono 6 */ },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.sonido), contentDescription = "Icono 6")
            }
            IconButton(onClick = { /* Navegar a la ventana del icono 7 */ },
                modifier = Modifier.clip(CircleShape).border(1.dp, Color.LightGray, CircleShape)) {
                Icon(painterResource(id = R.drawable.phone), contentDescription = "Icono 7")
            }
            IconButton(onClick = { /* Navegar a la ventana del icono 8 */ },
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
