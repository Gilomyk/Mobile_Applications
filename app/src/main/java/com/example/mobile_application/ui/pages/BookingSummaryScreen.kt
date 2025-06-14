package com.example.mobile_application.ui.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_application.model.TicketPayload
import com.example.mobile_application.viewmodel.BookingSummaryViewModel

// Composable
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSummaryScreen(
    showingId: Int,
    seatIds: List<Int>,
    onConfirmed: (String) -> Unit,
    viewModel: BookingSummaryViewModel = viewModel()
) {
    val movieTitle by viewModel.movieTitle.collectAsState()
    val cinemaName by viewModel.cinemaName.collectAsState()
    val date by viewModel.date.collectAsState()
    val time by viewModel.time.collectAsState()
    val showingPrice by viewModel.showingPrice.collectAsState()
    val seats by viewModel.selectedSeats.collectAsState()

    val discounts by viewModel.discounts.collectAsState()
    var email by remember { mutableStateOf("") }
    var tickets by remember { mutableStateOf<List<TicketPayload>>(emptyList()) }

    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadContext(showingId, seatIds)
        viewModel.loadDiscounts()
    }

    LaunchedEffect(seats) {
        tickets = seats.map { seat ->
            TicketPayload(
                showing = showingId,
                seat = seat.id,
                base_price = showingPrice.toDouble(),
                purchase_price = showingPrice.toDouble(),
                purchase_time = time,
                discount = null
            )
        }
    }

    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Booking Summary",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Movie", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    Text(movieTitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Date & Time", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    Text("$date $time", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Cinema", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    Text(cinemaName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }

        // Tickets list
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            tickets.forEachIndexed { idx, ticket ->
                val seatInfo = seats.find { it.id == ticket.seat }
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Row", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                            Text(seatInfo?.row?.toString() ?: "-", style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Seat", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                            Text(seatInfo?.number?.toString() ?: "-", style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Type", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                            // Dropdown for discount
                            var expanded by remember { mutableStateOf(false) }
                            Box {
                                Text(
                                    text = discounts.firstOrNull { it.id == ticket.discount }?.name ?: "Regular",
                                    modifier = Modifier
                                        .clickable { expanded = true }
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha=0.1f), shape = RoundedCornerShape(6.dp))
                                        .padding(8.dp)
                                )
                                DropdownMenu(expanded, { expanded = false }) {
                                    DropdownMenuItem(text = { Text("Regular") }, onClick = {
                                        expanded = false
                                        tickets = tickets.toMutableList().also {
                                            it[idx] = it[idx].copy(discount = null, purchase_price = showingPrice.toDouble())
                                        }
                                    })
                                    discounts.forEach { disc ->
                                        DropdownMenuItem(text = { Text("${disc.name} (-${disc.percentage.toInt()}%)") }, onClick = {
                                            expanded = false
                                            val price = ((100 - disc.percentage) * showingPrice / 100)
                                            tickets = tickets.toMutableList().also {
                                                it[idx] = it[idx].copy(discount = disc.id, purchase_price = price.toDouble())
                                            }
                                        })
                                    }
                                }
                            }
                        }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Price", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                            Text("${ticket.purchase_price.toInt()} PLN", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        // Total & email
        val total = tickets.sumOf { it.purchase_price }
        Text("Total: ${total.toInt()} PLN", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Text(error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
        }

        Button(
            onClick = {
                isLoading = true
                // launch coroutine: post tickets, then post order, then onConfirmed(paymentUrl)
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(if (isLoading) "Loading..." else "Confirm Booking")
        }
    }
}