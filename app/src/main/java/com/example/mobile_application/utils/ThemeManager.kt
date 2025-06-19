package com.example.mobile_application.utils

import android.app.Activity
import android.content.Context
import androidx.core.content.edit

object ThemeManager {
    private const val PREF_NAME = "theme_prefs"
    private const val KEY_DARK_MODE = "dark_mode"

    fun saveTheme(context: Context, isDark: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                putBoolean(KEY_DARK_MODE, isDark)
            }
    }

    fun isDarkTheme(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_DARK_MODE, false)
    }

    fun toggleAndApplyTheme(context: Context) {
        val newMode = !isDarkTheme(context)
        saveTheme(context, newMode)

        // Odśwież aktywność
        val activity = context as? Activity
        activity?.recreate()
    }
}
