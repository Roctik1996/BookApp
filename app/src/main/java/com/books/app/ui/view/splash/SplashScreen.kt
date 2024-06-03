package com.books.app.ui.view.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.books.app.R
import com.books.app.ui.theme.Georgia
import com.books.app.ui.theme.NunitoSans
import com.books.app.ui.theme.Pink40
import com.books.app.ui.theme.White
import com.books.app.ui.theme.WhiteAlpha20
import com.books.app.util.Constants.NAV_ROUTE_LIBRARY
import com.books.app.util.Constants.NAV_ROUTE_SPLASH
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {

    var startMainScreen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        startMainScreen = true
    }

    if (startMainScreen) {
        navController.navigate(NAV_ROUTE_LIBRARY) {
            popUpTo(NAV_ROUTE_SPLASH) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_heart),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = stringResource(R.string.book_app),
                fontSize = 52.sp,
                fontFamily = Georgia,
                fontWeight = FontWeight.Bold,
                color = Pink40,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.welcome_to_book_app),
                fontSize = 24.sp,
                fontFamily = NunitoSans,
                fontWeight = FontWeight.Bold,
                color = White,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(50.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                color = White,
                trackColor = WhiteAlpha20,
            )
        }
    }
}