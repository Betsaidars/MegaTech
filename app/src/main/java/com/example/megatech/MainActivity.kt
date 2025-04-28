package com.example.megatech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import com.example.megatech.Navigation.NavManager
import com.example.megatech.ViewModels.ListaDeDeseosViewModel
import com.example.megatech.ViewModels.ListaDeDeseosViewModelFactory
import com.example.megatech.ViewModels.LoginViewModel
import com.example.megatech.ViewModels.LoginViewModelFactory
import com.example.megatech.ui.theme.MegaTechTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager
    private val loginViewModel: LoginViewModel by viewModels(){
        LoginViewModelFactory(sessionManager)
    }
    private val listaDeDeseosViewModel: ListaDeDeseosViewModel by viewModels() {
        ListaDeDeseosViewModelFactory(this) // Asegúrate de que 'this' (el Context) no sea nulo aquí
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        // Establecer el locale español al inicio
        val locale = Locale("es")
        val localeList = LocaleListCompat.forLanguageTags(locale.language)
        AppCompatDelegate.setApplicationLocales(localeList)


        setContent {
            MegaTechTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                    {
                        NavManager(sessionManager, listaDeDeseosViewModel)
                    }
            }
        }
    }
}
