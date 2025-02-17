package com.example.megatech.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.megatech.ViewModels.LoginViewModel
import com.example.megatech.ViewModels.PrincipalViewModel
import com.example.megatech.Views.HomeView
import com.example.megatech.Views.Login.BlankView
import com.example.megatech.Views.Login.TabsView

@Composable
fun NavManager(loginVM: LoginViewModel, principalVM: PrincipalViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Blank" ){
        composable("Blank"){
            BlankView(navController)
        }
        composable("Login"){
            TabsView(navController, loginVM)
        }
        composable("Home"){
            HomeView(navController, principalVM)
        }
    }
}