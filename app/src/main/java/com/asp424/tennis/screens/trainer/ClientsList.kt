package com.asp424.tennis.screens.trainer


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.asp424.tennis.Blue300
import com.asp424.tennis.MainActivity
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.R
import com.asp424.tennis.room.User
import com.asp424.tennis.screens.*
import com.asp424.tennis.utils.getYearString


@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun DrawClientsScreen(
    mainActivity: MainActivity,
    navControllerTrainer: NavHostController,
    viewModel: MainViewModel
) {
    val listClientsVM: MutableList<User>? by viewModel.listClients.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Black else White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = "Ваши спортсмены")
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            listClientsVM?.forEach {
                var visible by remember { mutableStateOf(false) }
                val density = LocalDensity.current
                var buttonVisibility by remember { mutableStateOf(0.dp) }
                viewModel.checkUsersFor(it.uid) { check ->
                    if (check && it.token != "")
                        buttonVisibility = 60.dp
                }
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
                        color = when (it.color.toString()) {
                            "1" -> Red
                            "2" -> Blue300
                            "3" -> Green
                            "4" -> Yellow
                            else -> White
                        }
                    )
                ) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .background(
                                when (it.color.toString()) {
                                    "1" -> Red
                                    "2" -> Blue300
                                    "3" -> Green
                                    "4" -> Yellow
                                    else -> White
                                }
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it.name_child + " " + it.secondName_child, style = TextStyle(
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            modifier = Modifier.background(
                                when (it.color.toString()) {
                                    "1" -> Red
                                    "2" -> Blue300
                                    "3" -> Green
                                    "4" -> Yellow
                                    else -> White
                                }
                            ), color = when (it.color.toString()) {
                                "1" -> White
                                "2" -> White
                                "3" -> Black
                                "4" -> Black
                                else -> Blue
                            }
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
                            width = 2.dp, color = when (it.color.toString()) {
                                "1" -> Red
                                "2" -> Blue300
                                "3" -> Green
                                "4" -> Yellow
                                else -> White
                            }
                        ), elevation = 20.dp
                    ) {
                        Row(horizontalArrangement = Arrangement.End) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(id = R.string.content_desc),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        viewModel.deleteClientVM(it.uid)
                                    }
                            )
                        }
                        Column(
                            modifier = Modifier.padding(
                                top = 30.dp,
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 10.dp
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CellTittle(text = "Представитель")
                            CellText(text = it.name, tittle = "Имя: ")
                            CellText(text = it.secondName, tittle = "Фамилия: ")
                            CellText(text = it.patronymic, tittle = "Отчество: ")
                            SelectionContainer {
                                CellText(text = it.phone, tittle = "Телефон: ")
                            }
                            if (it.e_mail!!.isNotEmpty())
                            SelectionContainer {
                                CellText(text = it.e_mail, tittle = "eMail: ")
                            }
                            CellTittle(text = "Спортсмен")
                            if (it.name_group!!.isNotEmpty())
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Группа", style = TextStyle(fontSize = 17.sp))

                                    Text(
                                        text = it.name_group,
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontStyle = FontStyle.Italic,
                                            color = Blue
                                        )
                                    )
                                }
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
                            if (it.years!!.isNotEmpty())

                                Text(
                                    text = "(" + it.years.toString() + getYearString(
                                        it.years.toInt()
                                    ) + ")",
                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        textAlign = TextAlign.Center,
                                        fontStyle = FontStyle.Italic,
                                        color = Blue
                                    ), modifier = Modifier.padding(bottom = 2.dp, top = 2.dp)
                                )
                            ButtonInInfo(
                                text = "Добавить в расписание",
                                visibility = 60.dp,
                                action = {
                                    viewModel.apply {
                                        setDateStart(mutableListOf())
                                        setTimeStart("Выбрать время начала занятий")
                                        setTimeEnd("Выбрать время окончания занятий")
                                        setDesc("")
                                        it.name_child?.let { it1 -> setNotifyName("$it1 ${it.secondName_child}") }
                                        setNotifyId(it.uid)
                                        setGroupColor(it.color!!.toInt())
                                    }

                                    navControllerTrainer.navigate("Добавление в расписание") {
                                        popUpTo("Расписание") { inclusive = true }
                                    }
                                })

                            ButtonInInfo(
                                text = "Написать сообщение",
                                visibility = buttonVisibility,
                                action = {
                                    viewModel.getUserUidVM(it.uid) { uid ->
                                        viewModel._messages.value = mutableListOf()
                                        it.name_child?.let { it1 -> viewModel.setNotifyName("$it1 ${it.secondName_child}") }
                                        viewModel.setNotifyId(uid)
                                        viewModel.getMessagesVM(uid) {

                                        }
                                        navControllerTrainer.navigate("Чат")
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
    RoundButton(icon = Icons.Default.Add, action = {
        navControllerTrainer.navigate("Выбор метода")
    })
    DropdownTrainer(mainActivity, viewModel)
}

