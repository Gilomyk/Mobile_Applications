package com.example.mobile_application

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_application.ui.HomeScreen
import com.example.mobile_application.ui.theme.Mobile_ApplicationTheme

//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mobile_ApplicationTheme {
                HomeScreen(onMovieClick = { movie ->
                    // Obsługa przejścia np. do szczegółów
                    // np. navController.navigate("details/${movie.id}")
                })
            }
        }
    }
}
// Testowo dla strony z detalami
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            Mobile_ApplicationTheme {
//                MovieDetailsScreen(movieId = 1)
//            }
//        }
//    }
//}

