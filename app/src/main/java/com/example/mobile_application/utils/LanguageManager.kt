package com.example.mobile_application.utils

import android.content.Context
import androidx.core.content.edit
import java.util.Locale

object LanguageManager {
    private const val PREF_NAME = "language_prefs"
    private const val KEY_LANGUAGE = "lang"

    fun saveLanguage(context: Context, lang: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_LANGUAGE, lang)
            }
    }

    fun getLanguage(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, Locale.getDefault().language) ?: "en"
    }

    fun applyLanguage(context: Context, lang: String): Context {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        saveLanguage(context, lang)
        return context.createConfigurationContext(config)
    }

}
