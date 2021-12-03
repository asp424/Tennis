package com.asp424.tennis.screens.admin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asp424.tennis.Blue200
import com.asp424.tennis.Blue300
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.screens.trainer.TrainerScreen
import com.asp424.tennis.screens.user.UserScreen


@Composable
fun Test() {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "Один"
    ) {
        composable("Один") {
            FirstScreen()
        }
        composable("Два") {

        }
        composable("Три") {

        }
    }
}

@Composable
fun FirstScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Black else White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopAppBar(
            title = { Text(text = "Ваши спортсмены") },
            backgroundColor = if (isSystemInDarkTheme()) Blue300 else Blue200,
            contentColor = White,
            elevation = 2.dp
        )
        Column(
            Modifier.verticalScroll(rememberScrollState()).padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Первый экран")
        }
    }
}

