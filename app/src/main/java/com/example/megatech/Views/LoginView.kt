package com.example.megatech2.Views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.megatech.Model.UserModel
import com.example.megatech.R
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.LoginViewModel
import com.example.megatech.ViewModels.LoginViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(navController: NavController, sessionManager: SessionManager) {
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(sessionManager))

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val loginResult by loginViewModel.loginResult.collectAsState()
    val loginError by loginViewModel.loginError.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }

    val outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFFFFA7B1), // Cambia este color al que desees
        unfocusedBorderColor = MaterialTheme.colorScheme.outline, // Color del borde cuando no está enfocado
        cursorColor = Color(0xFFFFA7B1),
        focusedLabelColor = Color(0xFFFFA7B1)
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_principla),
            contentDescription = "Logo de MegaTech",
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            colors = outlinedTextFieldColors
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
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
                    Icon(
                        painter = painterResource(id = imageResource),
                        contentDescription = description
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val user = UserModel("", username, "", password)
            loginViewModel.loginUser(
                user = user,
                onSuccess = {
                    Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    navController.navigate("Main") // Navega a la pantalla principal
                },
                onFailure = { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA7B1)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                "Iniciar sesión",
            )
        }

        loginResult?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        loginError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
}