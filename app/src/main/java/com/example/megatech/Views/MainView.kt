package com.example.megatech.Views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainView(navController: NavController, sessionManager: SessionManager) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(sessionManager))

    val banners = mainViewModel.banners.collectAsState().value
    val pagerState = rememberPagerState(pageCount = { banners.size })

    LaunchedEffect(Unit) {
        mainViewModel.getAllBanner()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Ajusta la altura según sea necesario
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
                val color = if (pagerState.currentPage == index) Color.LightGray else Color.DarkGray
                Surface(
                    modifier = Modifier.size(10.dp),
                    shape = CircleShape,
                    color = color
                ) {}
            }
        }
    }
}
