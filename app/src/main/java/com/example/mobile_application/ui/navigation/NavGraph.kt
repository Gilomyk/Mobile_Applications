package com.example.mobile_application.ui.navigation

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobile_application.ui.pages.BookingSummaryScreen
import com.example.mobile_application.ui.pages.ClosestCinemaList
import com.example.mobile_application.ui.pages.HomeScreen
import com.example.mobile_application.ui.pages.LoginScreen
import com.example.mobile_application.ui.pages.MovieDetailsScreen
import com.example.mobile_application.ui.pages.MovieList
import com.example.mobile_application.ui.pages.PaymentSuccessScreen
import com.example.mobile_application.ui.pages.PaymentWebViewScreen
import com.example.mobile_application.ui.pages.ProfileScreen
import com.example.mobile_application.ui.pages.QRScreenWithPermission
import com.example.mobile_application.ui.pages.RegisterScreen
import com.example.mobile_application.ui.pages.SeatSelectionScreen
import com.example.mobile_application.ui.pages.SettingsScreen
import com.example.mobile_application.utils.AuthManager

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
                onLoginClick = {
                    navController.navigate("login")
                },
                onProfileClick = {
                    navController.navigate("profile")
                },
                onQrClick = {
                    navController.navigate("qrscanner")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
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
                    val encodedUrl = Uri.encode(paymentUrl)
                    navController.navigate("payment_web/$encodedUrl")
                }
            )
        }

        // wersja przeglądarkowa
        composable(
            "payment/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { entry ->
            val url = Uri.decode(entry.arguments?.getString("url")!!)
            val context = LocalContext.current

            // Otwarcie przeglądarki (prosto i skutecznie)
            LaunchedEffect(url) {
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                context.startActivity(intent)

                // Opcjonalnie wróć po chwili lub ręcznie
                navController.popBackStack() // jeśli chcesz wrócić
            }
        }

        // Wersja WebView
        composable(
            "payment_web/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { entry ->
            val url = Uri.decode(entry.arguments?.getString("url")!!)
            PaymentWebViewScreen(
                paymentUrl = url,
                onSuccess = {
                    navController.navigate("payment_success") {
                        popUpTo("summary") { inclusive = true }
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable("payment_success") {
            PaymentSuccessScreen(
                onReturnHome = {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true } // czyści stack jeśli chcesz
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home")
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(navController = navController)
        }

        composable("qrscanner") {
            QRScreenWithPermission(onScanned = {movieId ->
                navController.navigate("details/$movieId")
            })
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                authManager = AuthManager,
            )
        }

        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
