package com.example.mobile_application.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobile_application.ui.HomeScreen
import com.example.mobile_application.ui.pages.BookingSummaryScreen
import com.example.mobile_application.ui.pages.ClosestCinemaList
import com.example.mobile_application.ui.pages.MovieDetailsScreen
import com.example.mobile_application.ui.pages.MovieList
import com.example.mobile_application.ui.pages.SeatSelectionScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate("details/$movieId")
                },
                onLocationClick = {
                    navController.navigate("location")
                },
            )
        }

        composable(
            "details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetailsScreen(
                movieId = movieId,
                onShowtimeClick = { hallId, showingId ->
                    navController.navigate("seatSelection/$hallId/$showingId")
                }
            )
        }

        composable(
            "details/{movieId}/{cinemaId}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType },
                navArgument("cinemaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val cinemaId = backStackEntry.arguments?.getInt("cinemaId") ?: 0
            MovieDetailsScreen(
                movieId = movieId,
                cinemaId = cinemaId,
                onShowtimeClick = { hallId, showingId ->
                    navController.navigate("seatSelection/$hallId/$showingId")
                }
            )
        }

        composable(
            "seatSelection/{cinemaHallId}/{showingId}",
            arguments = listOf(
                navArgument("cinemaHallId") { type = NavType.IntType },
                navArgument("showingId") { type = NavType.IntType }
            )
        ) { back ->
            val hallId = back.arguments!!.getInt("cinemaHallId")
            val showId = back.arguments!!.getInt("showingId")
            SeatSelectionScreen(
                cinemaHallId = hallId,
                showingId = showId,
                onReserve = { seatIds ->
                    val seatIdParam = seatIds.joinToString(",")
                    navController.navigate("summary/$showId/$seatIdParam")
                }
            )
        }

        composable("location") {
            ClosestCinemaList(onCinemaClick = {cinemaId ->
                navController.navigate("movieListCinema/$cinemaId")
            })
        }

        composable("movieListCinema/{cinemaId}",
                arguments = listOf(navArgument("cinemaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val cinemaId = backStackEntry.arguments?.getInt("cinemaId") ?: 0
            MovieList(
                cinemaId =cinemaId,
                onMovieClick = {movieId ->
                navController.navigate("details/$movieId/$cinemaId")
            })
        }

        composable(
            "summary/{showingId}/{seatIds}",
            arguments = listOf(
                navArgument("showingId"){ type = NavType.IntType },
                navArgument("seatIds"){ type = NavType.StringType }
            )
        ) { back ->
            val showingId = back.arguments!!.getInt("showingId")
            val seatIds = back.arguments!!.getString("seatIds")!!
                .split(",").mapNotNull { it.toIntOrNull() }
            BookingSummaryScreen(
                showingId = showingId,
                seatIds = seatIds,
                onConfirmed = { paymentUrl ->
                    // otwórz WebView albo popBackStack
                }
            )
        }
    }
}
