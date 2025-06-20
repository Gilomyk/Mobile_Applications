package com.example.mobile_application.ui.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mobile_application.R
import com.example.mobile_application.ui.theme.LocalAppColors
import com.example.mobile_application.utils.AuthManager
import com.example.mobile_application.viewmodel.ProfileViewModel
import com.example.mobile_application.viewmodel.TicketWithDetails
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    navController: NavController,
    authManager: AuthManager,
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val appColors = LocalAppColors.current

    Column(
        Modifier
            .fillMaxSize()
            .background(appColors.background)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Header
        Row(
            Modifier
                .fillMaxWidth()
                .height(88.dp)
                .background(appColors.cardBackground)
                .padding(start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.profile),
                color = appColors.heading,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Button(
                onClick = {
                    authManager.clearToken(context)
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = appColors.primary)
            ) {
                Text(stringResource(R.string.logout), color = appColors.buttonText)
            }
        }

        Spacer(Modifier.height(16.dp))

        when {
            state.isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }

            state.error != null -> {
                Text(state.error!!, color = appColors.error)
            }

            else -> {
                // Dane użytkownika
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(appColors.cardBackground, RoundedCornerShape(12.dp))
                        .padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(appColors.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                state.user?.username?.firstOrNull()?.uppercase() ?: "",
                                color = appColors.buttonText,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column {
                            Text(state.user?.username ?: "", color = appColors.heading, fontSize = 20.sp)
                            Text(state.user?.email ?: "", color = appColors.metaText)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    stringResource(R.string.your_reservations),
                    color = appColors.heading,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(12.dp))

                if (state.tickets.isEmpty()) {
                    Text(stringResource(R.string.no_reservations), color = appColors.metaText)
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(state.tickets) { index, ticket ->
                            ReservationCard(ticket)
                            if (index != state.tickets.lastIndex) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationCard(ticketWithDetails: TicketWithDetails) {
    val ticket = ticketWithDetails.ticket
    val showing = ticketWithDetails.showing
    val movie = ticketWithDetails.movie

    val showingDate: OffsetDateTime = OffsetDateTime.parse(showing.date)
    val now = OffsetDateTime.now()
    val isActive = showingDate.isAfter(now)

    val appColors = LocalAppColors.current

    Row(
        Modifier
            .fillMaxWidth()
            .background(appColors.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
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
                .width(80.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(movie.title, color = appColors.heading, fontWeight = FontWeight.SemiBold)
            Text("${stringResource(R.string.seat)}: ${ticket.seat}", color = appColors.metaText)
            Text("${stringResource(R.string.price)}: ${ticket.purchase_price} zł", color = appColors.metaText)
            Text(
                "${stringResource(R.string.showing)}: ${showing.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}",
                color = appColors.metaText
            )
            Text(
                "${stringResource(R.string.hall)}: ${showing.hall}",
                color = appColors.metaText
            )
            Text(
                if (isActive) stringResource(R.string.active) else stringResource(R.string.expired),
                color = if (isActive) appColors.primary else appColors.disabledText
            )
        }
    }
}



