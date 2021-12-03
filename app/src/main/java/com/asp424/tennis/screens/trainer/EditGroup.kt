package com.asp424.tennis.screens.trainer

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.asp424.tennis.Blue200
import com.asp424.tennis.Blue300
import com.asp424.tennis.MainActivity
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.room.User
import com.asp424.tennis.screens.*

@Composable
fun EditGroup(
    viewModel: MainViewModel,
    navControllerTrainer: NavHostController,
    mainActivity: MainActivity
) {
    val listClientsVM: MutableList<User>? by viewModel.listClients.observeAsState(
        mutableListOf()
    )
    val groupName: String? by viewModel.groupName.observeAsState("")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = viewModel.getGroupName())
        Column(
            Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CellTittle(text = "Изменить название группы")
            CellOutField(value = groupName, text = "Название группы", KeyboardType.Text) { new ->
                viewModel.setGroupName(new)
            }
            CellTittle(text = "Изменить цвет группы")
            MenuColorEdit(viewModel = viewModel)
            CellTittle(text = "Ваши ученики")

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp, top = 6.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                listClientsVM?.forEach {
                    var stateCheck: Boolean? by remember {
                        mutableStateOf(false)
                    }
                    if (viewModel.checkState.value!![it.uid] == null)
                        viewModel.checkState.value!![it.uid] = false
                    stateCheck = viewModel.checkState.value!![it.uid]
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
                                    viewModel.checkState.value!![it.uid] =
                                        !viewModel.checkState.value!![it.uid]!!
                                    stateCheck = !stateCheck!!
                                    if (viewModel.checkState.value!![it.uid] == true) viewModel.setCheckListMap(
                                        it.uid,
                                        it.uid
                                    )
                                    else viewModel.getCheckListMap()
                                        .remove(it.uid)
                                }, modifier = Modifier.size(50.dp, 50.dp)
                            )

                            Column {
                                Text(
                                    text = it.name_child + " " + it.secondName_child,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontStyle = FontStyle.Italic
                                    ),
                                    modifier = Modifier.padding(start = 8.dp, bottom = 2.dp),
                                    color = Color.Blue
                                )
                            }
                        }
                    }
                    Card(
                        modifier = infoCard
                            .padding(start = 40.dp, bottom = 6.dp, top = 2.dp)
                            .shadow(20.dp),
                        border = BorderStroke(width = 2.dp, color = Color.Blue), elevation = 20.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CellTittle(text = "Представитель")
                            CellText(text = it.name, tittle = "Имя: ")
                            CellText(text = it.secondName, tittle = "Фамилия: ")
                            CellText(text = it.patronymic, tittle = "Отчество: ")
                            CellText(text = it.phone, tittle = "Телефон: ")
                            CellTittle(text = "Спортсмен")
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
                            CellText(text = it.years_old_child, tittle = "Дата рождения: ")
                        }
                    }
                }
            }

        }

    }
    RoundButton(icon = Icons.Default.ArrowForward, action = {
        viewModel.changeGroupVM(navControllerTrainer, mainActivity)
    })
}

@Composable
fun MenuColorEdit(viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val items =
        listOf(
            Color.Red,
            Blue300,
            Color.Green,
            Color.Yellow,
        )
    var selectedIndex by remember { mutableStateOf(viewModel.getGroupColor() - 1) }
    Card(
        border = BorderStroke(width = 0.5.dp, color = Color.Gray),
        modifier = Modifier
            .width(200.dp)
            .height(40.dp)
            .clickable(onClick = { expanded = true })
            .padding(bottom = 10.dp)
    ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "",
                Modifier
                    .background(items[selectedIndex])
                    .width(200.dp)
                    .height(40.dp),
                style = TextStyle(fontSize = 16.sp)
            )
            val colorGroup = when (items[selectedIndex]) {
                Color.Red -> {
                    1
                }
                Blue300 -> {
                    2
                }
                Color.Green -> {
                    3
                }
                Color.Yellow -> {
                    4
                }
                else -> 1
            }
            viewModel.setGroupColor(colorGroup)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.background(Color.Transparent)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {

                    selectedIndex = index
                    expanded = false
                }) {
                    Card(
                        border = BorderStroke(width = 0.5.dp, color = Color.Gray),
                        modifier = Modifier
                            .width(200.dp)
                            .height(40.dp)
                    ) {
                        Text(text = "", modifier = Modifier.background(s))
                    }
                }
            }
        }
    }
}