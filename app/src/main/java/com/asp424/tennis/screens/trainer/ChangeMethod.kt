package com.asp424.tennis.screens.trainer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.screens.TopBar

@Composable
fun SelectMethod(
    navControllerTrainer: NavHostController,
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = White)
    ) {
        TopBar(tittle = "Откуда добавить")
        Column(
            Modifier.fillMaxSize().background(if (isSystemInDarkTheme()) Color.Black else Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clickable {
                        viewModel.createClient()
                        viewModel.setYearsOldChild("Выберите день рождения")
                        navControllerTrainer.navigate("Создание клиента")
                    }, elevation = 10.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Добавить нового",
                        Modifier.padding(8.dp),
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .clickable {
                        viewModel.checkState.value = hashMapOf()
                        viewModel.setListClientsBD(mutableListOf())
                        viewModel.initialCheckListMap()
                        viewModel.getListClientsTrainerBD()
                        navControllerTrainer.navigate("Клиенты в БД")
                    }, elevation = 10.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Добавить из зарегестрированных",
                        Modifier.padding(8.dp),
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            }
        }
    }
}