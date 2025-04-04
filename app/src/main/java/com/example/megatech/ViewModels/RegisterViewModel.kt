package com.example.megatech.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.megatech.Model.UserModel
import com.example.megatech.RetrofitCliente
import com.example.megatech.SessionManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class RegisterViewModel(private val sessionManager: SessionManager) : ViewModel() {

    val username = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val passwordError = mutableStateOf(false)

    private val _registrationMessage = MutableStateFlow<String?>(null)
    val registrationMessage: StateFlow<String?> = _registrationMessage

    private val _registeredUserId = MutableStateFlow<String?>(null)
    val registeredUserId: StateFlow<String?> = _registeredUserId

    // Convertir StateFlow a LiveData
    val registrationMessageLiveData: LiveData<String?> = MutableLiveData<String?>().apply {
        viewModelScope.launch {
            registrationMessage.collect { value ->
                postValue(value)
            }
        }
    }

    val registeredUserIdLiveData: LiveData<String?> = MutableLiveData<String?>().apply {
        viewModelScope.launch {
            registeredUserId.collect { value ->
                postValue(value)
            }
        }
    }

    // SharedFlow para notificar el éxito del registro
    private val _registrationSuccess = MutableSharedFlow<Boolean>()
    val registrationSuccess: MutableSharedFlow<Boolean> = _registrationSuccess

    fun registerUser() {
        if (password.value == confirmPassword.value) {
            if (password.value.isNotEmpty()) {
                viewModelScope.launch {
                    try {
                        val userModel = UserModel(
                            id = null,
                            username = username.value,
                            email = email.value,
                            password = password.value
                        )
                        val response: Response<Map<String, Any>> =
                            RetrofitCliente.apiService.registerUser(userModel)

                        if (response.isSuccessful) {
                            _registrationMessage.value = response.body()?.get("message") as? String
                            val userDto = response.body()?.get("user") as? Map<*, *>
                            _registeredUserId.value = userDto?.get("id") as? String
                            _registrationSuccess.emit(true) // Emitir evento de éxito
                        } else {
                            _registrationMessage.value = "Error en el registro: ${response.errorBody()?.string()}"
                        }
                    } catch (e: HttpException) {
                        _registrationMessage.value = "Error de red: ${e.message()}"
                    } catch (e: Exception) {
                        _registrationMessage.value = "Error inesperado: ${e.message}"
                    }
                }
                passwordError.value = false
            } else {
                _registrationMessage.value = "La contraseña no puede estar vacía"
            }
        } else {
            passwordError.value = true
        }
    }
}