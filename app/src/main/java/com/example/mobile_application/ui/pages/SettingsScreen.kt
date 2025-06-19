package com.example.mobile_application.ui.pages

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_application.ui.theme.LocalAppColors
import com.example.mobile_application.utils.LanguageManager
import com.example.mobile_application.utils.ThemeManager

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val appColors = LocalAppColors.current
    val colorScheme = MaterialTheme.colorScheme

    var isDarkTheme by remember { mutableStateOf(ThemeManager.isDarkTheme(context)) }
    var selectedLanguage by remember { mutableStateOf(LanguageManager.getLanguage(context)) }
    val languageOptions = listOf("pl", "en")

    Column(
        Modifier
            .fillMaxSize()
            .background(appColors.background)
            .padding(24.dp)
    ) {
        // Header
        Row(
            Modifier
                .fillMaxWidth()
                .height(88.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Ustawienia", color = appColors.heading, fontSize = 24.sp)
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = appColors.icon
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Dark theme switch
        Row(
            Modifier
                .fillMaxWidth()
                .background(appColors.cardBackground, RoundedCornerShape(12.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ciemny motyw", color = appColors.text)
            Switch(
                checked = isDarkTheme,
                onCheckedChange = {
                    ThemeManager.toggleAndApplyTheme(context)
                    isDarkTheme = !isDarkTheme
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = appColors.primary
                )
            )
        }

        Spacer(Modifier.height(24.dp))

        // Language dropdown
        Row(
            Modifier
                .fillMaxWidth()
                .background(appColors.cardBackground, RoundedCornerShape(12.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Język", color = appColors.text)
            DropdownMenuBox(
                selected = selectedLanguage,
                options = languageOptions,
                onSelect = {
                    selectedLanguage = it
                    LanguageManager.saveLanguage(context, it)
                    val activity = (context as? Activity)
                    val intent = activity?.intent
                    activity?.finish()
                    activity?.startActivity(intent)
                }
            )
        }
    }
}


@Composable
fun DropdownMenuBox(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    val appColors = LocalAppColors.current

    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(selected.uppercase(), color = appColors.text)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it.uppercase(), color = appColors.text) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

