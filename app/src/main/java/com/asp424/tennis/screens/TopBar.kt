package com.asp424.tennis.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.asp424.tennis.Blue200
import com.asp424.tennis.Blue300

@Composable
fun TopBar(tittle: String){
    TopAppBar(
        title = {
            Text(text = tittle)
        },
        backgroundColor = if (isSystemInDarkTheme()) Blue300 else Blue200,
        contentColor = Color.White,
        elevation = 2.dp
    )
}