package com.example.mobile_application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(username: String, email: String, password1: String, password2: String) {
        _registerState.value = RegisterState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repository.register(username, email, password1, password2)
                if (response.isSuccessful) {
                    _registerState.value = RegisterState(success = true)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Błąd rejestracji"
                    _registerState.value = RegisterState(error = errorMsg)
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState(error = "Wyjątek: ${e.localizedMessage}")
            }
        }
    }
}
