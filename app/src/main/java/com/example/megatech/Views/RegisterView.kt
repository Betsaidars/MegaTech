package com.example.megatech2.Views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.RegisterViewModel
import com.example.megatech.ViewModels.RegisterViewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RegisterView(navController: NavController, sessionManager: SessionManager) {
    val registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(sessionManager))

    val registrationMessage by registerViewModel.registrationMessageLiveData.observeAsState()
    val registeredUserId by registerViewModel.registeredUserIdLiveData.observeAsState()

    // Observar el evento de éxito del registro
    LaunchedEffect(registerViewModel.registrationSuccess) {
        registerViewModel.registrationSuccess.collect { success ->
            if (success) {
                navController.navigate("main") // Navegar a la pantalla Main
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = registerViewModel.username.value,
            onValueChange = { registerViewModel.username.value = it },
            label = { Text("Nombre de Usuario") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = registerViewModel.email.value,
            onValueChange = { registerViewModel.email.value = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = registerViewModel.password.value,
            onValueChange = { registerViewModel.password.value = it },
            label = { Text("Contraseña") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = registerViewModel.confirmPassword.value,
            onValueChange = { registerViewModel.confirmPassword.value = it },
            label = { Text("Confirmar Contraseña") },
            isError = registerViewModel.passwordError.value
        )
        if (registerViewModel.passwordError.value) {
            Text(text = "Las contraseñas no coinciden", color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { registerViewModel.registerUser() }) {
            Text("Registrarse")
        }

        registrationMessage?.let {
            Text(text = it, color = if (it.startsWith("Error")) Color.Red else Color.Green)
        }

        registeredUserId?.let {
            Text(text = "ID de usuario registrado: $it", color = Color.Blue)
        }
    }
}