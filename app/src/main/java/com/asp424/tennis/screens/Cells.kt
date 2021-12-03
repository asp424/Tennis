package com.asp424.tennis.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.asp424.tennis.Blue200
import com.asp424.tennis.Blue300
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.R
import com.asp424.tennis.models.UserModel
import com.asp424.tennis.room.User
import com.asp424.tennis.utils.getYearString


//ClientList
@Composable
fun CellText(text: String?, tittle: String) {
    Row {
        Text(text = tittle, style = TextStyle(fontSize = 17.sp))
        text?.let { it1 ->
            Text(
                text = it1,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = Blue
                )
            )
        }
    }
}

@Composable
fun CellTittle(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            color = Blue
        ), modifier = Modifier.padding(bottom = 2.dp, top = 10.dp)
    )
}
@Composable
fun ButtonInInfo(
    text: String,
    visibility: Dp,
    action: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(visibility)
            .wrapContentSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(modifier = Modifier.padding(top = 10.dp), onClick = action, content = {
            Text(
                text = text,
                style = TextStyle(fontSize = 10.sp)
            )
        })
    }
}

@Composable
fun RoundButton(icon: ImageVector, action: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp, end = 60.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        OutlinedButton(onClick = action, modifier = Modifier.size(60.dp),
            shape = CircleShape, border = BorderStroke(1.dp, Color.Gray),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 20.dp,
                pressedElevation = 12.dp,
                disabledElevation = 5.dp
            )) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = R.string.content_desc)
            )
        } }}

@Composable
fun CellOutField(
    value: String?,
    text: String,
    keyboard: KeyboardType,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value!!,
        onValueChange = onValueChange, label = {
            Text(text = text)
        }, singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard)
    )
}

@ExperimentalAnimationApi
@Composable
fun CellGroup(
    list: MutableList<User>,
    colorBack: Color,
    textColor: Color,
    viewModel: MainViewModel,
    navControllerTrainer: NavHostController
) {
    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                visible = !visible
            }
            .height(if (list.isNotEmpty()) 70.dp else 0.dp)
            .padding(start = 8.dp, end = 8.dp, top = 3.dp),
            elevation = 10.dp,
            border = BorderStroke(
                width = 1.dp,
                color = if (isSystemInDarkTheme()) Blue300 else Blue200
            )) {
            Row(
                Modifier
                    .fillMaxSize()
                    .background(colorBack),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text =
                    if (list.isNotEmpty()) list[0].name_group.toString() else "",
                    modifier = Modifier.background(colorBack),
                    style = TextStyle(
                        color = textColor,
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp
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
                    width = 2.dp, color = colorBack
                ), elevation = 20.dp
            ) {
                Row(horizontalArrangement = Arrangement.Start) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.content_desc),
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                viewModel.setGroupName(list[0].name_group!!)
                                viewModel.initialCheckListMap()
                                viewModel.setGroupColor(
                                    when (colorBack) {
                                        Red -> 1
                                        Blue300 -> 2
                                        Color.Green -> 3
                                        Color.Yellow -> 4
                                        else -> 0
                                    }
                                )
                                viewModel.getGroupEditVM(list[0].color!!.toInt()) {
                                    navControllerTrainer.navigate("Редактирование группы") {
                                        popUpTo("Редактирование группы") { inclusive = true }
                                    }
                                }
                            }
                    )
                }
                Row(
                    modifier = Modifier.padding(start = 120.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.content_desc),
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                viewModel.setGroupColor(
                                    when (colorBack) {
                                        Red -> 1
                                        Blue300 -> 2
                                        Color.Green -> 3
                                        Color.Yellow -> 4
                                        else -> 0
                                    }
                                )
                                visible = false
                                viewModel.deleteGroup()
                            })
                }
                Column(
                    modifier = Modifier.padding(
                        top = 30.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp
                    ), horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Спортсмены", style = TextStyle(
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            color = Blue
                        )
                    )
                    list.forEach {
                        Row {
                            Column {
                                it.name_child?.let { it1 ->
                                    Text(
                                        text = it1 + " " + it.secondName_child,
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontStyle = FontStyle.Italic,
                                            color = Color.Black
                                        )
                                    )
                                }
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
                                        )
                                    )
                            }
                        }
                    }
                    ButtonInInfo(
                        text = "Добавить в расписание",
                        visibility = 60.dp,
                        action = {
                            viewModel.setDateStart(mutableListOf())
                            viewModel.setTimeStart("Выбрать время начала занятий")
                            viewModel.setTimeEnd("Выбрать время окончания занятий")
                            viewModel.setDesc("")
                            list[0].name_group?.let { it1 -> viewModel.setNotifyName(it1) }
                            viewModel.setNotifyId(list[0].uid)
                            viewModel.setGroupColor(list[0].color!!.toInt())
                            navControllerTrainer.navigate("Добавление в расписание группы") {
                                popUpTo("Добавление в расписание группы") { inclusive = true }
                            }
                        })
                }
            }
        }
    }
}

