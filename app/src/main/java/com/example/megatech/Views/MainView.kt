package com.example.megatech.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.MainViewModel
import com.example.megatech.ViewModels.MainViewModelFactory


@Composable
fun HomeView(navController: NavController, sessionManager: SessionManager) {

    val context = LocalContext.current // OBTENER CONTEXTO

    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(sessionManager))
    val banners by viewModel.banners.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.getAllBanner()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Banners")

//        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
//            items(banners) { banner ->
//                Image(
//                    painter = rememberImagePainter(banner.imageUrl),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(200.dp)
//                        .padding(8.dp)
//                        .clickable {
//                            // Navegar a la pantalla de detalles del banner (si es necesario)
//                        }
//                )
//            }
//        }
    }


}