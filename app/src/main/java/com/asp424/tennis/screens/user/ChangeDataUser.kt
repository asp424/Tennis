package com.asp424.tennis.screens.user

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tennis.*
import com.asp424.tennis.R
import com.asp424.tennis.screens.CellTittle
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.screens.trainer.DatePickerUser
import com.asp424.tennis.utils.showToast
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ChangeDataUser(
    viewModel: MainViewModel
) {
    val mainActivity = LocalContext.current as MainActivity
    val usernamePar: String? by viewModel.usernamePar.observeAsState("")
    val usernameChild: String? by viewModel.usernameChild.observeAsState("")
    val secondNameUserPar: String? by viewModel.secondNameUserPar.observeAsState("")
    val secondNameUserChild: String? by viewModel.secondNameUserChild.observeAsState("")
    val patronymicParUser: String? by viewModel.patronymicPar.observeAsState("")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = "Анкета")
        Box(Modifier.padding(top = 8.dp)) {
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CellTittle(text = "Введите Ваши данные")
                OutlinedTextField(
                    value = usernamePar!!,
                    onValueChange = { new ->
                        viewModel.setUsernamePar(new)
                    }, label = {
                        Text(text = stringResource(id = R.string.name))
                    }, singleLine = true
                )
                OutlinedTextField(
                    value = secondNameUserPar!!,
                    onValueChange = { new ->
                        viewModel.setSecondNameUserPar(new)
                    }, label = {
                        Text(text = stringResource(id = R.string.second_name))
                    }, singleLine = true
                )
                OutlinedTextField(
                    value = patronymicParUser!!,
                    onValueChange = { new ->
                        viewModel.setPatronymicUserPar(new)
                    }, label = {
                        Text(text = "Отчество")
                    }, singleLine = true
                )
                CellTittle(text = "Город, в котором будете заниматься")
                MenuCity(viewModel = viewModel)
                CellTittle(text = "Введите данные ребёнка")
                OutlinedTextField(
                    value = usernameChild!!,
                    onValueChange = { new ->
                        viewModel.setUsernameChild(new)
                    }, label = {
                        Text(text = "Имя")
                    }, singleLine = true
                )
                OutlinedTextField(
                    value = secondNameUserChild!!,
                    onValueChange = { new ->
                        viewModel.setSecondNameUserChild(new)
                    },
                    label = {
                        Text(text = stringResource(id = R.string.second_name))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                Card(modifier = Modifier
                    .width(280.dp)
                    .height(69.dp)
                    .padding(top = 10.dp)
                    .clickable {
                        mainActivity.setContent {
                            DatePickerUser(
                                mainActivity = mainActivity,
                                viewModel = viewModel
                            )
                        }
                    }, border = BorderStroke(1.dp, Gray)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()) {
                        Text(
                            text =  viewModel.getYearsOldChild(),
                            style = TextStyle(
                                fontSize = 17.sp,
                                textAlign = TextAlign.Center,
                                fontStyle = FontStyle.Italic,
                                color = Color.Blue
                            )
                        )
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp, end = 60.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        OutlinedButton(
            onClick = {
                CURRENT_UID = AUTH.currentUser?.uid.toString()
                viewModel.apply {
                    if (getUsernamePar().isNotEmpty() && getSecondNameUserPar().isNotEmpty() && getTrainUserCity().isNotEmpty()
                        && getUsernameChild().isNotEmpty() && getSecondNameUserChild().isNotEmpty() && getPatronymicPar().isNotEmpty() && getYearsOldChild().isNotEmpty()
                    ) {
                        val dateMap = mutableMapOf<String, Any>()
                        dateMap[CHILD_NAME] = getUsernamePar()
                        dateMap[CHILD_SECONDNAME] = getSecondNameUserPar()
                        dateMap[CHILD_PATRONYMIC] = getPatronymicPar()
                        dateMap[CHILD_NAME_CHILD] = getUsernameChild()
                        dateMap[CHILD_SECONDNAME_CHILD] = getSecondNameUserChild()
                        dateMap[CHILD_YEARS_CHILD] = getYearsOldChild()
                        dateMap[CHILD_CITY] = getTrainUserCity()
                        val years = SimpleDateFormat("yyyy").format(Date()).toInt() - viewModel.getYearsOldChild().substring(6, 10).toInt()
                        dateMap["years"] = years.toString()
                        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
                            .updateChildren(
                                dateMap
                            ).addOnCompleteListener {
                                mainActivity.setContent {
                                    UserScreen(
                                        viewModel = viewModel,
                                        id = ""
                                    )
                                }
                            }
                    } else showToast(
                        mainActivity.resources.getString(R.string.err_message),
                        mainActivity
                    )
                }

            },
            modifier = Modifier.size(60.dp),
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.Gray),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 20.dp,
                pressedElevation = 12.dp,
                disabledElevation = 5.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = stringResource(id = R.string.content_desc)
            )
        }
    }
}


@Composable
fun MenuCity(viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val items =
        listOf(
            stringResource(R.string.city1),
            stringResource(R.string.city2),
            stringResource(R.string.city3),
            stringResource(
                R.string.city4
            )
        )
    var selectedIndex by remember { mutableStateOf(viewModel.getTrainUserCity().toInt() - 1) }
    Card(
        modifier = Modifier
            .clickable(onClick = { expanded = true })
            .width(280.dp)
            .height(63.dp)
            .padding(top = 8.dp),
        border = BorderStroke(width = 0.5.dp, Gray)
    ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val city = when (items[selectedIndex]) {
                stringResource(id = R.string.city1) -> {
                    1
                }
                stringResource(id = R.string.city2) -> {
                    2
                }
                stringResource(id = R.string.city3) -> {
                    3
                }
                stringResource(id = R.string.city4) -> {
                    4
                }
                else -> 1
            }
            viewModel.setTrainUserCity(city.toString())
            Text(
                items[selectedIndex],
                Modifier.padding(start = 14.dp),
                style = TextStyle(fontSize = 16.sp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.background(Transparent)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                }) {
                    Card(
                        border = BorderStroke(width = 0.5.dp, color = Gray),
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(
                            text = s,
                            modifier = Modifier.padding(8.dp),
                            style = TextStyle(fontSize = 14.sp)
                        )
                    }
                }
            }
        }
    }
}
