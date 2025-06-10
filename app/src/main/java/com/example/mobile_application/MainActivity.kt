package com.example.mobile_application

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_application.ui.navigation.AppNavHost
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
                AppNavHost(
                    onMovieClick = { movieId ->
                        // nawigacja obsłużona w NavHost, więc przekazujemy dalej
                    }
                )
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

