package com.books.app.ui.view.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.books.app.ui.theme.Black
import com.books.app.ui.view.detail.DetailScreen
import com.books.app.ui.view.library.LibraryScreen
import com.books.app.ui.view.splash.SplashScreen
import com.books.app.util.Constants.KEY_BOOK_ID
import com.books.app.util.Constants.NAV_ROUTE_DETAIL
import com.books.app.util.Constants.NAV_ROUTE_LIBRARY
import com.books.app.util.Constants.NAV_ROUTE_SPLASH

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Black
            ) {
                val navController = rememberNavController()
                NavHostScreen(navController)
            }
        }
    }
}

@Composable
fun NavHostScreen(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NAV_ROUTE_SPLASH) {
        composable(NAV_ROUTE_SPLASH) { SplashScreen(navController) }
        composable(NAV_ROUTE_LIBRARY) { LibraryScreen(navController) }
        composable(NAV_ROUTE_DETAIL) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString(KEY_BOOK_ID)?.toInt() ?: 0
            DetailScreen(navController, bookId)
        }
    }
}