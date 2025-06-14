package com.example.mobile_application

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_application.ui.navigation.AppNavHost
import com.example.mobile_application.ui.theme.Mobile_ApplicationTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mobile_ApplicationTheme() {
                AppNavHost()
            }
        }
    }
}

