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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaymentSuccessScreen(
    onReturnHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A28)) // tło
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFF1E2433), RoundedCornerShape(16.dp))
                .padding(32.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✅ Płatność zakończona sukcesem!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF43F08C),
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Dziękujemy za zakup. Twój bilet został zarezerwowany.",
                fontSize = 16.sp,
                color = Color(0xFFE9ECEF),
                modifier = Modifier.padding(bottom = 32.dp),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onReturnHome,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4318D1),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(text = "Wróć na stronę główną", fontWeight = FontWeight.Medium)
            }
        }
    }
}
