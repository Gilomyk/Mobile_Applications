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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_application.R
import com.example.mobile_application.model.TicketPayload
import com.example.mobile_application.ui.theme.AppColors
import com.example.mobile_application.ui.theme.LocalAppColors
import com.example.mobile_application.viewmodel.BookingSummaryViewModel
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

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
    val colorScheme = MaterialTheme.colorScheme
    val appColors = LocalAppColors.current

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

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

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

    Column(
        Modifier
            .fillMaxSize()
            .background(appColors.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.booking_summary),
            style = MaterialTheme.typography.headlineSmall,
            color = appColors.heading,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = appColors.cardBackground)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoRow(stringResource(R.string.movie), movieTitle, appColors)
                InfoRow(stringResource(R.string.datetime), "$date $time", appColors)
                InfoRow(stringResource(R.string.cinema), cinemaName, appColors)
            }
        }

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
                    colors = CardDefaults.cardColors(containerColor = appColors.cardBackground)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoRow(stringResource(R.string.row), seatInfo?.row?.toString() ?: "-", appColors)
                        InfoRow(stringResource(R.string.seat_label), seatInfo?.number?.toString() ?: "-", appColors)

                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text(stringResource(R.string.type), style = MaterialTheme.typography.bodyMedium, color = appColors.metaText)
                            var expanded by remember { mutableStateOf(false) }

                            Box {
                                Text(
                                    text = discounts.firstOrNull { it.id == ticket.discount }?.name ?: stringResource(R.string.discount_regular),
                                    modifier = Modifier
                                        .clickable { expanded = true }
                                        .background(appColors.discountBg, shape = RoundedCornerShape(6.dp))
                                        .padding(8.dp),
                                    color = appColors.text
                                )
                                DropdownMenu(expanded, { expanded = false }) {
                                    DropdownMenuItem(text = { Text(stringResource(R.string.discount_regular)) }, onClick = {
                                        expanded = false
                                        tickets = tickets.toMutableList().also {
                                            it[idx] = it[idx].copy(discount = null, purchase_price = showingPrice.toDouble())
                                        }
                                    })
                                    discounts.forEach { disc ->
                                        DropdownMenuItem(text = {
                                            Text("${disc.name} (-${disc.percentage.toInt()}%)")
                                        }, onClick = {
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

                        InfoRow(stringResource(R.string.price_label), "${ticket.purchase_price.toInt()} PLN", appColors)
                    }
                }
            }
        }

        val total = tickets.sumOf { it.purchase_price }
        Text("Total: ${total.toInt()} PLN", style = MaterialTheme.typography.headlineSmall, color = appColors.heading, modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.booking_email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Text(it, color = appColors.error, modifier = Modifier.padding(8.dp))
        }

        Button(
            onClick = {
                isLoading = true
                error = null
                coroutineScope.launch {
                    try {
                        val now = OffsetDateTime.now().toString()
                        val ticketPayloads = tickets.map { it.copy(purchase_time = now) }
                        val ticketIds = viewModel.postTickets(ticketPayloads)
                        if (ticketIds.isEmpty()) {
                            error = "Ticket creation failed."
                            isLoading = false
                            return@launch
                        }
                        val orderResponse = viewModel.postOrder(ticketIds, email)
                        if (orderResponse == null) {
                            error = "Order creation failed."
                            isLoading = false
                            return@launch
                        }
                        onConfirmed(orderResponse.payment_url)
                    } catch (e: Exception) {
                        error = "Unexpected error: ${e.localizedMessage}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(if (isLoading) stringResource(R.string.loading) else stringResource(R.string.confirm_booking))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, appColors: AppColors) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = appColors.metaText)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = appColors.text)
    }
}
