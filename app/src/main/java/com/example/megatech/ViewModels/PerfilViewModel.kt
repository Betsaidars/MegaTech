package com.example.megatech.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.megatech.Model.UserModel
import com.example.megatech.RetrofitCliente
import com.example.megatech.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PerfilViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    private val _updateNameEmailResult = MutableStateFlow<Boolean?>(null)
    val updateNameEmailResult: StateFlow<Boolean?> = _updateNameEmailResult

    private val _changePasswordResult = MutableStateFlow<Boolean?>(null)
    val changePasswordResult: StateFlow<Boolean?> = _changePasswordResult

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()


    fun setPasswordError(message: String) {
        _passwordError.value = message
    }

    fun fetchUserData() {
        val currentUser = sessionManager.getUserModel()
        _user.value = currentUser
    }

    fun updateNameEmail(newName: String, newEmail: String) {
        viewModelScope.launch {
            try {
                user.value?.id?.let { userId ->
                    val updatedUser = UserModel(id = userId, username = newName, email = newEmail, password = "") // No enviamos la contraseña
                    Log.d("PerfilViewModel", "Updating user with ID in body: ${updatedUser.id}, name: $newName, email: $newEmail") // Agrega este log
                    val response = RetrofitCliente.apiService.updateUser(userId, updatedUser) // <--- AQUÍ ESTÁ LA LLAMADA
                    if (response.isSuccessful) {
                        _updateNameEmailResult.value = true
                        val updatedUserInfo = response.body()
                        updatedUserInfo?.let { sessionManager.saveUserModel(mapApiUserToAppUser(it)) }
                    } else {
                        _updateNameEmailResult.value = false
                        Log.e("ProfileViewModel", "Error al actualizar el nombre/email: ${response.code()}")
                    }
                } ?: run {
                    _updateNameEmailResult.value = false
                    Log.e("ProfileViewModel", "ID de usuario no encontrado")
                }
            } catch (e: Exception) {
                _updateNameEmailResult.value = false
                Log.e("ProfileViewModel", "Error al llamar a la API para actualizar el nombre/email: ${e.message}")
            } finally {
                _updateNameEmailResult.value = null // Resetear el estado
            }
        }
    }

    fun changePassword(newPassword: String) {
        viewModelScope.launch {
            try {
                user.value?.id?.let { userId ->
                    // Asumiendo que tienes un endpoint específico para cambiar la contraseña
                    val passwordUpdate = mapOf("newPassword" to newPassword)
                    _changePasswordResult.value = true
                    _passwordError.value = null

                } ?: run {
                    _changePasswordResult.value = false
                    _passwordError.value = "ID de usuario no encontrado"
                    Log.e("ProfileViewModel", "ID de usuario no encontrado para cambiar la contraseña")
                }
            } catch (e: Exception) {
                _changePasswordResult.value = false
                _passwordError.value = "Error al llamar a la API para cambiar la contraseña: ${e.message}"
                Log.e("ProfileViewModel", "Error al llamar a la API para cambiar la contraseña: ${e.message}")
            } finally {
                _changePasswordResult.value = null // Resetear el estado
            }
        }
    }
}

fun mapApiUserToAppUser(apiUser: UserModel): UserModel {
    return UserModel(
        id = apiUser.id,
        username = apiUser.username,
        email = apiUser.email,
        password = ""
    )
}