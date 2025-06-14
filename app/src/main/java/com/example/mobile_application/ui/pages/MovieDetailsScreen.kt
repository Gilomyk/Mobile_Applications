package com.example.mobile_application.ui.pages

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mobile_application.R
import com.example.mobile_application.model.Cinema
import com.example.mobile_application.model.CinemaHall
import com.example.mobile_application.model.HallType
import com.example.mobile_application.model.Movie
import com.example.mobile_application.model.MovieCrew
import com.example.mobile_application.model.MovieShowing
import com.example.mobile_application.ui.theme.CrewBg
import com.example.mobile_application.ui.theme.CrewBorderAccent
import com.example.mobile_application.ui.theme.GenrePurple
import com.example.mobile_application.ui.theme.getMetaTextColor
import com.example.mobile_application.viewmodel.CinemaViewModel
import com.example.mobile_application.viewmodel.MovieDetailsViewModel
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    cinemaId: Int?=null,
    onShowtimeClick: (Int, Int) -> Unit,
    viewModel: MovieDetailsViewModel = viewModel(),
    cinemaViewModel: CinemaViewModel = viewModel(),
) {
    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
        if (cinemaId == null)
            cinemaViewModel.fetchCinemas()
        else
            cinemaViewModel.fetchCinema(cinemaId)
    }

    val movie by viewModel.movie.collectAsState()
    val crew by viewModel.crew.collectAsState()
    val cinemas by viewModel.cinemas.collectAsState()
    val cinemaHalls by viewModel.cinemaHalls.collectAsState()
    val hallTypes by viewModel.hallTypes.collectAsState()
    val showingsByDate by viewModel.showingsByDate.collectAsState()
    val loadingShowtimes by viewModel.loadingShowtimes.collectAsState()

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

        var selectedTab by remember { mutableStateOf(0) }
        val tabs = listOf("Opis", "Seanse")

        Column {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> DetailsTab(m = movie!!, c = crew!!)
                1 -> ShowingsTab(
                    movieId = movieId,
                    viewModel = viewModel,
                    cinemaId = cinemaId,
                    cinemas = cinemas,
                    cinemaHalls =  cinemaHalls,
                    hallTypes = hallTypes,
                    showingsByDate = showingsByDate,
                    loading = loadingShowtimes,
                    onShowtimeClick = onShowtimeClick
                )
            }
        }
    }
}

@Composable
fun DetailsTab (m: Movie, c: MovieCrew) {

    val scrollState = rememberScrollState()

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
        if (m.genre.isNotEmpty()) {
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
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Reżyser i główna rola
        SectionBlock(title = "Twórcy") {
            CrewInfoItem(label = "Reżyser", value = c.director.joinToString { it.name })
            CrewInfoItem(label = "Główna rola", value = c.main_lead.joinToString { it.name })
        }
    }
}

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowingsTab(
    movieId: Int,
    viewModel: MovieDetailsViewModel,
    cinemaId: Int?=null,
    cinemas: List<Cinema>,
    cinemaHalls: List<CinemaHall>,
    hallTypes: List<HallType>,
    showingsByDate: Map<String, List<MovieShowing>>,
    loading: Boolean,
    onShowtimeClick: (Int, Int) -> Unit
) {
    val dates = remember { (0 until 30).map { LocalDate.now().plusDays(it.toLong()) } }
    var selectedDate by remember { mutableStateOf(dates.first()) }
    var selectedCinema by remember { mutableStateOf<Cinema?>(null) }

    LaunchedEffect(cinemaId) {
        cinemaId?.let {
            selectedCinema = cinemas.find { cinema -> cinema.id == it }
        }
    }

    // wczytaj seansy przy wyborze
    LaunchedEffect(selectedDate, selectedCinema) {
        if (selectedCinema != null) {
            viewModel.loadShowingsForDate(movieId, selectedDate, selectedCinema!!.id)
        } else {
            viewModel.loadShowingsForDate(movieId, selectedDate)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // === Wybór kina ===
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            cinemas.forEach { cinema ->
                Button(
                    onClick = { selectedCinema = cinema.copy() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (cinema == selectedCinema) GenrePurple else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(cinema.name, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // DatePicker
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
            dates.forEach { date ->
                val label = date.format(DateTimeFormatter.ofPattern("EEE, dd"))
                Button(
                    onClick = { selectedDate = date },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (date == selectedDate) GenrePurple else MaterialTheme.colorScheme.surface
                    )
                ) { Text(label, color = MaterialTheme.colorScheme.onSurface) }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (selectedCinema != null) {
            val showings = showingsByDate[selectedDate.toString()].orEmpty()
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    items = showings,
                    span = { _, _ -> GridItemSpan(5) }
                ) { _, showing ->
                    ShowtimeCard(showing, hallTypes, onClick = {
                        onShowtimeClick(showing.hall, showing.id)
                    })
                }
            }
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Wybierz kino, aby zobaczyć seanse")
            }
        }
    }
}

@Composable
fun MetadataItem(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 14.sp, color = getMetaTextColor(isSystemInDarkTheme()))
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
                    withStyle(style = SpanStyle(MaterialTheme.colorScheme.onBackground)) {
                        append(value)
                    }
                },
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowtimeCard(
    showing: MovieShowing,
    hallTypes: List<HallType>,
    onClick: () -> Unit = {}
) {
    val timeFormatted = try {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        OffsetDateTime.parse(showing.date).toLocalTime().format(formatter)
    } catch (e: Exception) {
        "Brak godziny"
    }

    val hallTypeName = hallTypes.firstOrNull { it.id == showing.showing_type }?.hall_type ?: "Sala"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(text = timeFormatted, style = MaterialTheme.typography.titleMedium)
            Text(text = hallTypeName, style = MaterialTheme.typography.bodySmall)
            Text(
                text = "${showing.ticket_price} PLN",
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}



