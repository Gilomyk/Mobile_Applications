package com.example.mobile_application.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.layout.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.*
import com.example.mobile_application.R
import com.example.mobile_application.ui.theme.*
import com.example.mobile_application.viewmodel.MovieDetailsViewModel

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = viewModel()
) {
    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    val movie by viewModel.movie.observeAsState()
    val crew by viewModel.crew.observeAsState()

    val scrollState = rememberScrollState()

    if (movie == null || crew == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        val m = movie!!
        val c = crew!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Tytuł filmu
            Text(
                text = m.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Plakat
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = m.poster.replace("http://", "https://"),
                        placeholder = painterResource(R.drawable.placeholder_image),
                        error = painterResource(R.drawable.placeholder_image)
                    ),
                    contentDescription = "Movie Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Metadane filmu
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetadataItem(label = "Premiera", value = m.release_date)
                MetadataItem(label = "Czas trwania", value = "${m.duration} min")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gatunki
            if (!m.genre.isNullOrEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    m.genre.forEach {
                        Text(
                            text = it.genre,
                            color = GenrePurple,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .background(CrewBg, shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Opis filmu
            SectionBlock(title = "Opis") {
                Text(
                    text = m.description,
                    color = MovieDescription,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reżyser i główna rola
            SectionBlock(title = "Twórcy") {
                CrewInfoItem(label = "Reżyser", value = c.director?.joinToString { it.name })
                CrewInfoItem(label = "Główna rola", value = c.mainLead?.joinToString { it.name })
            }
        }
    }
}


@Composable
fun MetadataItem(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 14.sp, color = MetaText)
        Text(text = value, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun SectionBlock(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun CrewInfoItem(label: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CrewBg, shape = RoundedCornerShape(8.dp))
                .border(2.dp, CrewBorderAccent, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = CrewBorderAccent, fontWeight = FontWeight.SemiBold)) {
                        append("$label: ")
                    }
                    withStyle(style = SpanStyle(color = CrewText)) {
                        append(value)
                    }
                },
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

