package com.example.megatech2.Views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.megatech.R

@Composable
fun RegisterView(navController: NavController, sessionManager: SessionManager) {
    val registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(sessionManager))

    val registrationMessage by registerViewModel.registrationMessageLiveData.observeAsState()
    val registeredUserId by registerViewModel.registeredUserIdLiveData.observeAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFFFFA7B1),
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        cursorColor = Color(0xFFFFA7B1),
        focusedLabelColor = Color(0xFFFFA7B1)
    )

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
            label = { Text("Nombre de Usuario") },
            colors = outlinedTextFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = registerViewModel.email.value,
            onValueChange = { registerViewModel.email.value = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = outlinedTextFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = registerViewModel.password.value,
            onValueChange = { registerViewModel.password.value = it },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = outlinedTextFieldColors,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val imageResource = if (passwordVisible)
                    R.drawable.visibility // Reemplaza con el nombre de tu icono de ojo abierto
                else
                    R.drawable.visibility_off

                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = painterResource(id = imageResource), contentDescription = description)
                }
            }

        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = registerViewModel.confirmPassword.value,
            onValueChange = { registerViewModel.confirmPassword.value = it },
            label = { Text("Confirmar Contraseña") },
            isError = registerViewModel.passwordError.value,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Oculta la contraseña por defecto
            colors = outlinedTextFieldColors,
            trailingIcon = {
                val imageResource = if (passwordVisible)
                    R.drawable.visibility // Reemplaza con el nombre de tu icono de ojo abierto
                else
                    R.drawable.visibility_off

                val description = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(painter = painterResource(id = imageResource), contentDescription = description)
                }
            }
        )
        if (registerViewModel.passwordError.value) {
            Text(text = "Las contraseñas no coinciden", color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { registerViewModel.registerUser() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA7B1),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
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