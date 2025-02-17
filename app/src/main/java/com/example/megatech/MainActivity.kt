package com.example.megatech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.megatech.Navigation.NavManager
import com.example.megatech.ViewModels.LoginViewModel
import com.example.megatech.ViewModels.PrincipalViewModel
import com.example.megatech.Views.Login.LoginView
import com.example.megatech.Views.Login.TabsView
import com.example.megatech.ui.theme.MegaTechTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginVM : LoginViewModel by viewModels()
        val principalVM : PrincipalViewModel by viewModels()

        setContent {
            MegaTechTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                    {
                        NavManager(loginVM, principalVM)
                    }
            }
        }
    }
}
