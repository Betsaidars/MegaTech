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
                    val updatedUser =
                        UserModel(id = userId, username = newName, email = newEmail, password = "")
                    Log.d(
                        "PerfilViewModel",
                        "Updating user with ID in body: ${updatedUser.id}, name: $newName, email: $newEmail"
                    )
                    val response = RetrofitCliente.apiService.updateUser(userId, updatedUser)
                    if (response.isSuccessful) {
                        _updateNameEmailResult.value = true
                        val updatedUserInfo = response.body()
                        updatedUserInfo?.let {
                            val newUserModel = mapApiUserToAppUser(it)
                            sessionManager.saveUserModel(newUserModel)
                            _user.value = newUserModel // Emitir una NUEVA instancia del UserModel
                        }
                    } else {
                        _updateNameEmailResult.value = false
                        Log.e(
                            "ProfileViewModel",
                            "Error al actualizar el nombre/email: ${response.code()}"
                        )
                    }
                } ?: run {
                    _updateNameEmailResult.value = false
                    Log.e("ProfileViewModel", "ID de usuario no encontrado")
                }
            } catch (e: Exception) {
                _updateNameEmailResult.value = false
                Log.e(
                    "ProfileViewModel",
                    "Error al llamar a la API para actualizar el nombre/email: ${e.message}"
                )
            } finally {
                _updateNameEmailResult.value = null // Resetear el estado
            }
        }
    }

    fun changePassword(newPassword: String) {
        viewModelScope.launch {
            try {
                user.value?.id?.let { userId ->
                    val passwordUpdate = mapOf("newPassword" to newPassword)
                    val response = RetrofitCliente.apiService.changePassword(
                        userId,
                        passwordUpdate
                    ) // <--- ASUME QUE TIENES ESTA FUNCIÓN EN TU ApiService

                    if (response.isSuccessful) {
                        _changePasswordResult.value = true
                        _passwordError.value = null
                        Log.d("PerfilViewModel", "Contraseña cambiada exitosamente")
                        // Opcional: Podrías limpiar los campos de contraseña en la UI aquí si lo deseas
                    } else {
                        _changePasswordResult.value = false
                        val errorBody = response.errorBody()?.string()
                            ?: "Error desconocido al cambiar la contraseña"
                        _passwordError.value = errorBody
                        Log.e(
                            "PerfilViewModel",
                            "Error al cambiar la contraseña: ${response.code()} - $errorBody"
                        )
                    }

                } ?: run {
                    _changePasswordResult.value = false
                    _passwordError.value = "ID de usuario no encontrado"
                    Log.e(
                        "PerfilViewModel",
                        "ID de usuario no encontrado para cambiar la contraseña"
                    )
                }
            } catch (e: Exception) {
                _changePasswordResult.value = false
                _passwordError.value =
                    "Error al llamar a la API para cambiar la contraseña: ${e.message}"
                Log.e(
                    "PerfilViewModel",
                    "Error al llamar a la API para cambiar la contraseña: ${e.message}"
                )
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
