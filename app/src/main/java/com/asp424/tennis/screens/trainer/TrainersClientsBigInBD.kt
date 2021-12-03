package com.asp424.tennis.screens.trainer

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.asp424.tennis.Blue200
import com.asp424.tennis.Blue300
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.models.UserModel
import com.asp424.tennis.screens.*
import com.asp424.tennis.utils.getYearString


@Composable
fun ClientsInBDScreenBig(
    viewModel: MainViewModel,
    navControllerTrainer: NavHostController
) {
    val listClientsVM: MutableList<UserModel>? by viewModel.listClientsBDBig.observeAsState(
        mutableListOf()
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center
    ) {
        TopBar(tittle = "Выберите спортсменов")
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                ,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            listClientsVM?.forEach {
                var stateCheck: Boolean? by remember {
                    mutableStateOf(false)
                }
                if (viewModel.checkState.value!![it.id] == null)
                    viewModel.checkState.value!![it.id] = false
                stateCheck = viewModel.checkState.value!![it.id]
                var infoCard by remember { mutableStateOf(Modifier.size(0.dp, 0.dp)) }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        infoCard = if (infoCard == Modifier.wrapContentSize()) Modifier.size(
                            width = 0.dp,
                            height = 0.dp
                        ) else Modifier.wrapContentSize()
                    }
                    .height(70.dp)
                    .padding(start = 8.dp, end = 8.dp, top = 3.dp),
                    elevation = 10.dp,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSystemInDarkTheme()) Blue300 else Blue200
                    )) {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = stateCheck!!,
                            onCheckedChange = { _ ->
                                viewModel.checkState.value!![it.id] =
                                    !viewModel.checkState.value!![it.id]!!
                                stateCheck = !stateCheck!!
                                if (viewModel.checkState.value!![it.id] == true) viewModel.setCheckListMap(
                                    it.key,
                                    it.id
                                )
                                else viewModel.getCheckListMap()
                                    .remove(it.key)
                            }, modifier = Modifier.size(50.dp, 50.dp)
                        )
                        Column {
                            Text(
                                text = it.name + " " + it.secondName,
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    textAlign = TextAlign.Center,
                                    fontStyle = FontStyle.Italic,
                                    color = Blue
                                )
                            )
                        }
                    }
                }
                Card(
                    modifier = infoCard
                        .padding(start = 40.dp, bottom = 6.dp, top = 2.dp)
                        .shadow(20.dp),
                    border = BorderStroke(width = 2.dp, color = Blue), elevation = 20.dp
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CellText(text = it.name, tittle = "Имя: ")
                        CellText(text = it.secondName, tittle = "Фамилия: ")
                        CellText(text = it.patronymic, tittle = "Отчество: ")
                        CellText(text = it.phone, tittle = "Телефон: ")
                        CellText(
                            text = when (it.city_of_training) {
                                "1" -> "Фрязино"
                                "2" -> "Щёлково"
                                "3"
                                -> "Ивантеевка"
                                "4"
                                -> "Балашиха"
                                else -> "Не выбран"
                            }, tittle = "Город: "
                        )
                    }
                }
            }
        }
    }
    RoundButton(icon = Icons.Default.Add, action = {
        viewModel.addClientFromBDVM(navControllerTrainer)
    })
}






