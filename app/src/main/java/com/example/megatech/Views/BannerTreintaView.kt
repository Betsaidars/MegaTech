package com.example.megatech.Views

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.BannerTreintaViewModel
import com.example.megatech.ViewModels.BannerTreintaViewModelFactory
import com.example.megatech.ViewModels.MainViewModel
import com.example.megatech.ViewModels.MainViewModelFactory

@OptIn(UnstableApi::class)
@Composable
fun BannerTreintaView(navController: NavController){
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val bannerTreintaViewModel: BannerTreintaViewModel = viewModel(factory = BannerTreintaViewModelFactory(sessionManager))
    val discountedItems by bannerTreintaViewModel.discountedItemsTreinta.collectAsState()
    val isLoading by bannerTreintaViewModel.isLoadingDiscountedItemsTreinta.collectAsState()
    val error by bannerTreintaViewModel.errorLoadingDiscountedItemsTreinta.collectAsState()

    LaunchedEffect(key1 = Unit) {
        bannerTreintaViewModel.getDiscountedItemsTreinta()
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
                text = "Items con 30% de Descuento",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(discountedItems) { itemWithDiscount ->
                    Log.d(
                        "BannerTreintaView",
                        "Image URL: ${itemWithDiscount.imageUrlDiscoint}"
                    )
                    Column(
                        modifier = Modifier
                            .clickable {
                                navController.navigate("itemDetail/${itemWithDiscount.idDiscoint}")
                            }
                            .padding(8.dp)
                    ) {
                        val painterState = remember {
                            mutableStateOf<AsyncImagePainter.State>(
                                AsyncImagePainter.State.Loading(
                                    null
                                )
                            )
                        }

                        Image(
                            painter = rememberAsyncImagePainter(
                                model = itemWithDiscount.imageUrlDiscoint ?: ""
                            ),
                            contentDescription = itemWithDiscount.nameDiscoint,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = itemWithDiscount.nameDiscoint,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "${itemWithDiscount.normalPriceDiscoint} €",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Text(
                            text = "${itemWithDiscount.priceWithDiscoint} €",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}