package com.example.mobile_application.viewmodel

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application.network.ApiClient
import com.example.mobile_application.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val repository = UserRepository(application)

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        _loginState.value = LoginState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repository.login(username, password)
                if (response.isSuccessful) {
                    val token = response.body()?.access
                    // Zapisz token w SharedPreferences itp.
                    _loginState.value = LoginState(success = true)
                } else if(response.code() == 401) {
                    _loginState.value = LoginState(error = "Podano błędne dane logowania")
                } else {
                    _loginState.value = LoginState(error = "Błąd logowania: ${response.code()}")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState(error = "Wyjątek: ${e.localizedMessage}")
            }
        }
    }
}
