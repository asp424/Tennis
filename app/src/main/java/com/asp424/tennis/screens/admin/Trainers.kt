package com.asp424.tennis.screens.admin

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.asp424.tennis.*
import com.asp424.tennis.room.TrainerMod
import com.asp424.tennis.screens.*
import com.asp424.tennis.screens.trainer.DropdownTrainer
import com.asp424.tennis.utils.AppValueEventListener
import com.asp424.tennis.utils.getTrainerModel

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun DrawTrainersScreen(
    mainActivity: MainActivity,
    navControllerAdmin: NavHostController,
    viewModel: MainViewModel
) {
    val listTrainersVM: MutableList<TrainerMod>? by viewModel.listTrainers.observeAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = "Тренеры")
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            listTrainersVM?.forEach {
                var visible by remember { mutableStateOf(false) }
                val density = LocalDensity.current
                var gpsButtonVisibility by remember { mutableStateOf(0.dp) }
                REF_DATABASE_ROOT.child(NODE_TRAINERS).child(it.uid)
                    .addListenerForSingleValueEvent(AppValueEventListener { trainer ->
                        if (trainer.getTrainerModel().gps_time.toString().isNotEmpty())
                            gpsButtonVisibility = 40.dp
                    })
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            visible = !visible
                        }
                        .height(70.dp)
                        .padding(start = 8.dp, end = 8.dp, top = 3.dp),
                    elevation = 10.dp,
                    border = BorderStroke(
                        width = 1.dp,
                        color = Blue300
                    )
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it.name + " " + it.secondName, style = TextStyle(
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(
                        // Slide in from 40 dp from the top.
                        initialOffsetY = { with(density) { -40.dp.roundToPx() } }
                    ) + expandVertically(
                        // Expand from the top.
                        expandFrom = Alignment.Top
                    ) + fadeIn(
                        // Fade in with the initial alpha of 0.3f.
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(start = 40.dp, bottom = 6.dp, top = 2.dp)
                            .shadow(20.dp),
                        border = BorderStroke(
                            width = 2.dp, color = Blue300
                        ), elevation = 20.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                top = 10.dp,
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 10.dp
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CellText(text = it.name, tittle = "Имя: ")
                            CellText(text = it.secondName, tittle = "Фамилия: ")
                            CellText(text = it.patronymic, tittle = "Отчество: ")
                            CellText(text = it.phone, tittle = "Телефон: ")
                            CellText(text = it.whatsapp_number, tittle = "Номер в WhatsApp: ")
                            CellText(text = it.e_mail, tittle = "Е-mail: ")
                            CellText(text = it.workPlace, tittle = "Места работы: ")
                            ButtonInInfo(
                                text = "Посмотреть местоположение",
                                visibility = gpsButtonVisibility,
                                action = {
                                    it.name?.let { it1 -> viewModel.setNotifyName("$it1 ${it.secondName}") }
                                    viewModel.setNotifyId(it.uid)
                                    navControllerAdmin.navigate("Координаты")
                                })
                            ButtonInInfo(
                                text = "Посмотреть расписание",
                                visibility = 40.dp,
                                action = {
                                    it.name?.let { it1 -> viewModel.setNotifyName("$it1 ${it.secondName}") }
                                    viewModel.setNotifyId(it.uid)
                                    viewModel.getListOfEventsForAdmin(it.uid) {
                                        navControllerAdmin.navigate("Расписание тренера")
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
    DropdownTrainer(mainActivity, viewModel)
}