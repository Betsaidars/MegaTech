package com.example.megatech.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.megatech.SessionManager
import com.example.megatech.Views.TabsView
import com.example.megatech.Views.MainView

@Composable
fun NavManager(sessionManager: SessionManager){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Login") {

        composable("Login") {
            TabsView(navController, sessionManager)
        }

        composable("Register") {
            TabsView(navController, sessionManager)
        }

        composable("Main"){
            MainView(navController, sessionManager)
        }

    }

}
