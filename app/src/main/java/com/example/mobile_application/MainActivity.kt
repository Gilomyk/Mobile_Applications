package com.example.mobile_application

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_application.ui.navigation.AppNavHost
import com.example.mobile_application.ui.theme.Mobile_ApplicationTheme
import com.example.mobile_application.utils.LanguageManager
import com.example.mobile_application.utils.ThemeManager
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isDark = ThemeManager.isDarkTheme(this)
        setContent {
            Mobile_ApplicationTheme(darkTheme = isDark) {
                AppNavHost()
            }
        }
    }
    override fun attachBaseContext(newBase: Context) {
        val lang = LanguageManager.getLanguage(newBase)
        super.attachBaseContext(LanguageManager.applyLanguage(newBase, lang))
    }
}

