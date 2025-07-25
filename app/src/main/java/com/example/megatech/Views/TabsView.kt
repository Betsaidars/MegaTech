package com.example.megatech.Views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.LoginViewModel
import com.example.megatech2.Views.LoginView
import com.example.megatech2.Views.RegisterView

@Composable
fun TabsView(navController: NavController, sessionManager: SessionManager){
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Iniciar Sesión", "Registrarse")
    val indicatorColor = Color(0xFFFFA7B1)

    Column {
        TabRow(selectedTabIndex = selectedTab,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = indicatorColor
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(text = title) },
                )
            }
        }
        when(selectedTab){
            0 -> LoginView(navController, sessionManager)
            1 -> RegisterView(navController, sessionManager)
        }
    }
}