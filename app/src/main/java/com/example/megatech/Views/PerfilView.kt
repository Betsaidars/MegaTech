package com.example.megatech.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.megatech.R
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.PerfilViewModel
import com.example.megatech.ViewModels.PerfilViewModelFactory

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilView(navController: NavController, sessionManager: SessionManager) {

    val perfilViewModel: PerfilViewModel = viewModel(factory = PerfilViewModelFactory(sessionManager))

    val user by perfilViewModel.user.collectAsState()
    val name = remember(user?.username) { mutableStateOf(user?.username ?: "") }
    val email = remember(user?.email) { mutableStateOf(user?.email ?: "") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    val updateNameEmailResult by perfilViewModel.updateNameEmailResult.collectAsState(initial = null)
    val changePasswordResult by perfilViewModel.changePasswordResult.collectAsState(initial = null)
    val passwordError by perfilViewModel.passwordError.collectAsState()

    LaunchedEffect(Unit) {
        perfilViewModel.fetchUserData()
    }

    LaunchedEffect(updateNameEmailResult) {
        if (updateNameEmailResult == true) {
            // Mostrar mensaje de éxito y recargar datos si es necesario
            perfilViewModel.fetchUserData()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, stringResource(R.string.back))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Formulario para actualizar nombre y email
            Text(stringResource(R.string.name))
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = { perfilViewModel.updateNameEmail(name.value, email.value) }) {
                Text(stringResource(R.string.save_changes))
            }
            if (updateNameEmailResult == true) {
                Text(stringResource(R.string.update_success), color = MaterialTheme.colorScheme.secondary)
            } else if (updateNameEmailResult == false) {
                Text(stringResource(R.string.update_failed), color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Formulario para cambiar contraseña
            Text(stringResource(R.string.new_password))
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text(stringResource(R.string.new_password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = confirmNewPassword,
                onValueChange = { confirmNewPassword = it },
                label = { Text(stringResource(R.string.confirm_new_password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (newPassword == confirmNewPassword && newPassword.isNotEmpty()) {
                        perfilViewModel.changePassword(newPassword)
                    } else {
                        // Mostrar error si las contraseñas no coinciden
                        val errorMessage = "Las contraseñas no coinciden"
                        perfilViewModel.setPasswordError(errorMessage)
                    }
                }
            ) {
                Text(stringResource(R.string.change_password))
            }
            if (changePasswordResult == true) {
                Text(stringResource(R.string.password_changed_success))
            } else if (changePasswordResult == false) {
                Text(passwordError ?: stringResource(R.string.password_change_failed))
            }
        }
    }
}