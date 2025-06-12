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
import com.example.mobile_application.ui.pages.MovieDetailsScreen
import com.example.mobile_application.ui.pages.SeatSelectionScreen
import com.example.mobile_application.ui.pages.ClosestCinemaList

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
                    navController.navigate("summary/${showId}/${seatIds.joinToString(",")}")
                }
            )
        }

        composable("location") {
            ClosestCinemaList()
        }
    }
}
