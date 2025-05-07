package com.example.megatech.Views

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.BannerCincuentaViewModel
import com.example.megatech.ViewModels.BannerCincuentaViewModelFactory
import com.example.megatech.ViewModels.BannerTreintaViewModel
import com.example.megatech.ViewModels.BannerTreintaViewModelFactory

@OptIn(UnstableApi::class)
@Composable
fun BannerCincuentaView(navController: NavController){
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val bannerCincuentaViewModel: BannerCincuentaViewModel = viewModel(factory = BannerCincuentaViewModelFactory(sessionManager))
    val discountedFiftyItems by bannerCincuentaViewModel.discountedItemsCincuenta.collectAsState()
    val isLoading by bannerCincuentaViewModel.isLoadingDiscountedItemsCincuenta.collectAsState()
    val error by bannerCincuentaViewModel.errorLoadingDiscountedItemsCincuenta.collectAsState()

    LaunchedEffect(key1 = Unit) {
        bannerCincuentaViewModel.getDiscountedItemsCincuenta()
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
            Text(
                text = "Items con 50% de Descuento",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
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