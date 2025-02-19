package com.example.megatech.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.megatech.R

@Composable
fun PrincipalView(navController: NavController){
    Column {
        Banner(
            image = R.drawable.sale_banner,
            onClick = { navController.navigate("productos_rebajados") }
        )
    }
}

@Composable
fun Banner(image: Int, onClick: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.width(16.dp))
    }
}