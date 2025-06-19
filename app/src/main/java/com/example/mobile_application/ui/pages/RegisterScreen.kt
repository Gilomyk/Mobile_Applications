package com.example.mobile_application.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobile_application.R
import com.example.mobile_application.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.registerState.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F1C))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create Account", color = Color.White, fontSize = 24.sp)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text(stringResource(R.string.username)) })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text(stringResource(R.string.email)) })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text(stringResource(R.string.password)) }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text(stringResource(R.string.confirm_password)) }, visualTransformation = PasswordVisualTransformation())

        if (state.error != null) {
            Text(text = state.error ?: "", color = Color.Red, fontSize = 14.sp)
        }

        Button(
            onClick = {
                if (password == confirmPassword) {
                    viewModel.register(username, email, password, confirmPassword)
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.sign_up))
        }

        TextButton(onClick = { navController.navigate("login") }) {
            Text(stringResource(R.string.already_have_account))
        }
    }

    if (state.success) {
        // Można dodać delay(1000) i przejść automatycznie
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }
}
