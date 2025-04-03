package com.example.megatech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.megatech.Navigation.NavManager
import com.example.megatech.ViewModels.LoginViewModel
import com.example.megatech.ViewModels.LoginViewModelFactory
import com.example.megatech.ui.theme.MegaTechTheme

class MainActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager
    private val loginViewModel: LoginViewModel by viewModels(){
        LoginViewModelFactory(sessionManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        setContent {
            MegaTechTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                    {
                        NavManager(sessionManager)
                    }
            }
        }
    }
}
