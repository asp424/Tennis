package com.asp424.tennis.screens.user

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.asp424.tennis.*
import com.asp424.tennis.room.TrainerMod
import com.asp424.tennis.screens.ButtonInInfo
import com.asp424.tennis.screens.CellText
import com.asp424.tennis.screens.CellTittle
import com.asp424.tennis.screens.TopBar

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun UsersTrainerScreen(
    mainActivity: MainActivity,
    navControllerUser: NavHostController,
    viewModel: MainViewModel
) {
    val listTrainersVM: MutableList<TrainerMod>? by viewModel.listTrainers.observeAsState()
    Column(
        modifier = Modifier.fillMaxSize().background(if (isSystemInDarkTheme()) Blue300 else Blue200),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = "Ваши тренеры")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 59.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (listTrainersVM!!.isNotEmpty()) {
                listTrainersVM?.forEach {
                    Card(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 3.dp),
                        elevation = 10.dp,
                        border = BorderStroke(
                            width = 1.dp,
                            color = Blue300
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CellText(text = it.name, tittle = "Имя: ")
                            CellText(text = it.secondName, tittle = "Фамилия: ")
                            CellText(text = it.patronymic, tittle = "Отчество: ")
                            SelectionContainer {
                                CellText(text = it.phone, tittle = "Телефон: ")
                            }
                            ButtonInInfo(
                                text = "Написать сообщение",
                                visibility = 60.dp,
                                action = {
                                    viewModel._messages.value = mutableListOf()
                                    viewModel.setNotifyName(it.name + " " + it.patronymic + " " + it.secondName)
                                    viewModel.setNotifyId(it.id!!)

                                    viewModel.getUserKeyVM { key ->
                                        viewModel._userKey.value = key
                                        viewModel.getMessagesUserVM(CURRENT_UID, it.id) {
                                        }
                                    }
                                    navControllerUser.navigate("Чат")
                                })
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 59.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, end = 8.dp, top = 3.dp),
                        elevation = 10.dp,
                        border = BorderStroke(
                            width = 1.dp,
                            color = Blue300
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CellTittle(text = "Тренер ещё не назначен")
                        }
                    }
                }
            }

        }
    }
    DropdownUser(mainActivity, viewModel, navControllerUser)
}