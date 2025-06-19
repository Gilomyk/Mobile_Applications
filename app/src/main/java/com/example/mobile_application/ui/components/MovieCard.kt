package com.example.mobile_application.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mobile_application.R
import com.example.mobile_application.model.Movie
import com.example.mobile_application.ui.theme.LocalAppColors


@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
    val appColors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = movie.poster.replace("http://", "https://"),
                placeholder = painterResource(R.drawable.placeholder_image),
                error = painterResource(R.drawable.error_image)
            ),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = appColors.text,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "${movie.duration} min",
            style = MaterialTheme.typography.bodySmall,
            color = appColors.textSecondary
        )
    }
}
