package com.example.mobile_application.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_application.ui.theme.AccentPurple
import com.example.mobile_application.ui.theme.AvailableColor
import com.example.mobile_application.ui.theme.JustTakenColor
import com.example.mobile_application.ui.theme.MetaText
import com.example.mobile_application.ui.theme.SelectedColor
import com.example.mobile_application.ui.theme.TakenColor
import com.example.mobile_application.viewmodel.SeatSelectionViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

@Composable
fun SeatSelectionScreen(
    showingId: Int,
    cinemaHallId: Int,
    onReserve: (List<Int>) -> Unit,
    viewModel: SeatSelectionViewModel = viewModel()
) {
    val seats by viewModel.seats.collectAsState()
    val taken by viewModel.takenSeatIds.collectAsState()
    val selected by viewModel.selectedSeatIds.collectAsState()
    val justTaken by viewModel.justTakenSeatIds.collectAsState()

    LaunchedEffect(cinemaHallId) { viewModel.loadSeats(cinemaHallId) }
    LaunchedEffect(showingId) { viewModel.loadTakenSeats(showingId) }

    LaunchedEffect(showingId) {
        val client = OkHttpClient()
        val request = Request.Builder().url("wss://127.0.0.1:8000/ws/movie_showings/$showingId/").build()
        client.newWebSocket(request, object: WebSocketListener() {
            override fun onMessage(ws: WebSocket, text: String) {
                val seatId = JSONObject(text).getJSONObject("data").getInt("seat_id")
                viewModel.markSeatJustTaken(seatId)
            }
        })
    }


    // WebSocket: w LaunchedEffect pokaż jak subskrybować i wywoływać viewModel.markSeatJustTaken

    val grouped = remember(seats) {
        seats.groupBy { it.row }.toSortedMap()
    }
    val maxPerRow = grouped.values.maxOfOrNull { it.size } ?: 0

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Ekran (np. "screen" bar na górze)
        Box(
            Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(AccentPurple)
        )

        Spacer(Modifier.height(16.dp))

        // 🔁 PRZEWIJALNA SEKCJA Z SIEDZENIAMI
        Box(
            modifier = Modifier
                .weight(1f) // ← Rozciąga do przycisku na dole
                .verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp) // dodatkowe paddingi, jeśli chcesz
        ) {
            Column(Modifier.align(Alignment.Center)) {
                // Nagłówki kolumn
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(Modifier.width(32.dp)) // dla numeru rzędu
                    repeat(maxPerRow) { idx ->
                        Box(
                            modifier = Modifier
                                .size(40.dp), // taki sam rozmiar jak siedzenie
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                textAlign = TextAlign.Center,
                                color = MetaText
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Rzędy z siedzeniami
                grouped.forEach { (rowNum, rowSeats) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "$rowNum",
                            color = MetaText,
                            modifier = Modifier.width(32.dp),
                            fontWeight = FontWeight.Bold
                        )

                        rowSeats.sortedBy { it.number }.forEach { seat ->
                            val isTaken = seat.id in taken
                            val isSelected = seat.id in selected
                            val isJust = seat.id in justTaken

                            Box(
                                Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            isJust -> JustTakenColor
                                            isTaken -> TakenColor
                                            isSelected -> SelectedColor
                                            else -> AvailableColor
                                        }
                                    )
                                    .clickable(enabled = !isTaken) {
                                        viewModel.toggleSelect(seat.id)
                                    }
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        // 🔘 PRZYCISK REZERWACJI
        Button(
            onClick = { onReserve(selected.toList()) },
            enabled = selected.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Rezerwuj (${selected.size})")
        }
    }
}
