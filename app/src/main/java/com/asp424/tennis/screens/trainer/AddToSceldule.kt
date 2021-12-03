package com.asp424.tennis.screens.trainer

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.asp424.tennis.*
import com.asp424.tennis.screens.CellOutField
import com.asp424.tennis.screens.CellTittle
import com.asp424.tennis.screens.RoundButton
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.utils.AppValueEventListener
import com.asp424.tennis.utils.getTrainerModel
import com.asp424.tennis.utils.getUserModel
import com.asp424.tennis.utils.showToast

@Composable
fun ScreenAddToSchedule(
    viewModel: MainViewModel,
    mainActivity: MainActivity,
    navControllerTrainer: NavHostController
) {
    val desc: String? by viewModel._desc.observeAsState("")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = viewModel.getNotifyName())
        Box(Modifier.padding(top = 8.dp)) {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CellTittle(text = "Даты занятия")
                Card(modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        navControllerTrainer.navigate("Дата начала")
                    }
                    .padding(top = 6.dp), border = BorderStroke(2.dp, color = Color.Blue)) {
                    Text(
                        text = "Выбрать дату начала занятий",
                        style = TextStyle(
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic,
                            color = Color.Black
                        ), modifier = Modifier.padding(14.dp)
                    )
                }
                viewModel.getDateStart().forEach {date ->
                    Card(modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 6.dp), border = BorderStroke(2.dp, color = Color.Blue)) {
                        Text(
                            text = date,
                            style = TextStyle(
                                fontSize = 17.sp,
                                textAlign = TextAlign.Center,
                                fontStyle = FontStyle.Italic,
                                color = Color.Black
                            ), modifier = Modifier.padding(14.dp)
                        )
                    }
                }

                CellTittle(text = "Время начала занятия")
                Card(modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        navControllerTrainer.navigate("Время начала")
                    }
                    .padding(top = 6.dp), border = BorderStroke(2.dp, color = Color.Blue)) {
                    Text(
                        text = viewModel.getTimeStart(),
                        style = TextStyle(
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic,
                            color = Color.Black
                        ), modifier = Modifier.padding(14.dp)
                    )


                }
                CellTittle(text = "Время окончания занятия")
                Card(modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        navControllerTrainer.navigate("Время окончания")

                    }
                    .padding(top = 6.dp), border = BorderStroke(2.dp, color = Color.Blue)) {
                    Text(
                        text = viewModel.getTimeEnd(),
                        style = TextStyle(
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic,
                            color = Color.Black
                        ), modifier = Modifier.padding(14.dp)
                    )

                }

                CellTittle(text = "Заметки")
                CellOutField(
                    value = desc,
                    text = "Заметки",
                    KeyboardType.Text
                ) { new ->
                    viewModel.setDesc(new)
                }
            }

        }
    }
    RoundButton(icon = Icons.Default.ArrowForward, action = {
        if (viewModel.getTimeEnd() != "Выбрать время окончания занятий"
            && viewModel.getTimeStart() != "Выбрать время начала занятий"
            &&
            viewModel.getTimeEnd() != ""
            && viewModel.getTimeStart() != ""

        ) {
            if (viewModel.getTimeEnd() < viewModel.getTimeStart())
                showToast(
                    "Время окончания не может быть больше времени начала занятия",
                    mainActivity
                )
            else {
                viewModel.getDateStart().forEach { date ->
                    val dateMap = hashMapOf<String, Any>()
                    dateMap["name"] = "Спортсмен: " + viewModel.getNotifyName()
                    if (viewModel.getGroupColor().toString().isEmpty())
                        dateMap["color"] = 0
                    else dateMap["color"] = viewModel.getGroupColor()
                    dateMap["start"] = date + "T" + viewModel.getTimeStart()
                    dateMap["end"] = date + "T" + viewModel.getTimeEnd()
                    dateMap["desc"] = viewModel.getDesc()
                    dateMap["client"] = viewModel.getNotifyId()
                    dateMap["mark"] = "user"
                    REF_DATABASE_ROOT.child("trainers").child(CURRENT_UID).child("events").child(
                        REF_DATABASE_ROOT.push().key.toString()
                    ).updateChildren(dateMap).addOnCompleteListener {
                        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID)
                            .addListenerForSingleValueEvent(AppValueEventListener { it1 ->
                                val nameTrainer =
                                    "Тренер: " + it1.getTrainerModel().name + it1.getTrainerModel().secondName
                                REF_DATABASE_ROOT.child(NODE_USERS)
                                    .addListenerForSingleValueEvent(AppValueEventListener {
                                        it.children.forEach { user ->
                                            if (user.getUserModel().key == viewModel.getNotifyId()) {
                                                val map = hashMapOf<String, Any>()
                                                map["name"] = nameTrainer
                                                map["start"] =
                                                    date + "T" + viewModel.getTimeStart()
                                                map["end"] =
                                                    date + "T" + viewModel.getTimeEnd()
                                                map["desc"] = ""
                                                map["mark"] = "user"
                                                map["trainer"] = CURRENT_UID
                                                REF_DATABASE_ROOT.child(NODE_USERS)
                                                    .child(user.getUserModel().id)
                                                    .child("events")
                                                    .child(REF_DATABASE_ROOT.push().key.toString())
                                                    .updateChildren(map)
                                                    .addOnCompleteListener {
                                                        navControllerTrainer.navigate("Расписание")
                                                    }
                                            }
                                        }
                                    })
                            })
                    }
                }
            }
        } else showToast("Выберите дату и время", mainActivity)
        })
    }

