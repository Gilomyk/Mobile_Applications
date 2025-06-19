package com.example.mobile_application.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_application.R
import com.example.mobile_application.ui.theme.LocalAppColors

@Composable
fun PaymentSuccessScreen(
    onReturnHome: () -> Unit
) {
    val appColors = LocalAppColors.current
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appColors.cardBackground) // zamiast Color(0xFF151A28)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(appColors.surfaceVariant, RoundedCornerShape(16.dp)) // zamiast Color(0xFF1E2433)
                .padding(32.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.payment_success),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF43F08C), // Zielony sukces – zostaje hardcoded, bo specyficzny
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.thank_you),
                fontSize = 16.sp,
                color = appColors.heading, // zamiast Color(0xFFE9ECEF)
                modifier = Modifier.padding(bottom = 32.dp),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onReturnHome,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColors.primary,
                    contentColor = appColors.buttonText
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.back_to_home),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

