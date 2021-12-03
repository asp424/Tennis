package com.asp424.tennis.screens.user

import android.os.Build
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asp424.tennis.*
import com.asp424.tennis.R
import com.asp424.tennis.screens.change_profile.ChangeScreen
import com.asp424.tennis.screens.trainer.WeekScheduleTheme
import com.asp424.tennis.utils.appData
import com.asp424.tennis.utils.setStateScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun UserScreen(
    viewModel: MainViewModel,
    id: String
) {
    var notif = id
    val mainActivity = LocalContext.current as MainActivity
    val db = LocalContext.current.appData
    viewModel.clearData()
    setStateScreen(mainActivity, 6)
    var rasp by remember {
        mutableStateOf(true)
    }
    var tren by remember {
        mutableStateOf(false)
    }
    var pay by remember {
        mutableStateOf(false)
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
    ) {
        val navControllerUser = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigation(backgroundColor = if (isSystemInDarkTheme()) Blue300 else Blue200) {
                    BottomNavigationItem(
                        selected = rasp,
                        onClick = {
                            rasp = true
                            tren = false
                            pay = false
                            navControllerUser.navigate("Расписание") {
                                popUpTo("Расписание") { inclusive = true }
                            }
                        },
                        label = { Text(text = "Расписание") },
                        icon = {
                            Icon(Icons.Default.Schedule, "ass")
                        }, modifier = Modifier.fillMaxSize()
                    )
                    BottomNavigationItem(
                        selected = tren,
                        onClick = {
                            rasp = false
                            tren = true
                            pay = false
                            navControllerUser.navigate("Тренер") {
                                popUpTo("Тренер") { inclusive = true }
                            }
                        },
                        label = { Text(text = "Тренер") },
                        icon = {
                            Icon(Icons.Default.SportsTennis, "ass")
                        }, modifier = Modifier.fillMaxSize()
                    )
                    BottomNavigationItem(
                        selected = pay,
                        onClick = {
                            rasp = false
                            tren = false
                            pay = true
                            navControllerUser.navigate("Оплата") {
                                popUpTo("Оплата") { inclusive = true }
                            }
                        },
                        label = { Text(text = "Оплата") },
                        icon = {
                            Icon(Icons.Default.Payment, "ass")
                        }, modifier = Modifier.fillMaxSize()
                    )

                }
            }
        ) {
            DropdownUser(mainActivity, viewModel, navControllerUser)
            NavHost(navController = navControllerUser, startDestination = "Расписание") {
                composable("Тренер") {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        viewModel.setListTrainers(mutableListOf())
                        CircularProgressIndicator()
                    }
                    viewModel.getUsersTrainerVM()
                    CoroutineScope(Dispatchers.IO).launch {
                        if (db.getAllTrainers().isNotEmpty())
                            viewModel.setListTrainers(db.getAllTrainers())
                    }
                    UsersTrainerScreen(
                        mainActivity = mainActivity,
                        navControllerUser = navControllerUser,
                        viewModel = viewModel
                    )
                }
                composable("Оплата") {

                }

                composable("Расписание") {
                    if (notif.isNotEmpty()) {
                        viewModel.getTrainerName(id = notif) {
                            viewModel.setNotifyName(it)
                            viewModel.setNotifyId(notif)
                            notif = ""
                            navControllerUser.navigate("Чат")
                        }
                    } else {
                        viewModel.eventList.value = mutableListOf()

                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                        viewModel.getListOfEventsUser(navControllerUser, mainActivity) {
                            navControllerUser.navigate("Set")
                        }
                    }
                }
                composable("Set") {
                    var scale by remember { mutableStateOf(1f) }
                    val state = rememberTransformableState { zoomChange, _, _ ->
                        scale *= zoomChange

                    }
                    WeekScheduleTheme {
                        Surface(color = MaterialTheme.colors.background) {
                            viewModel.eventList.value?.let { it1 ->
                                ScheduleUser(
                                    state = state,
                                    scale = scale,
                                    events = it1
                                )
                            }
                        }
                    }
                }
                composable("Чат") {
                    ChatUser(viewModel = viewModel)
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun DropdownUser(
    mainActivity: MainActivity,
    viewModel: MainViewModel,
    navControllerUser: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
            .padding(top = 3.dp)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.Default.MoreVert,
                tint = White,
                contentDescription = stringResource(id = R.string.content_desc)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                CoroutineScope(Dispatchers.IO).launch { expanded = false }
            }
        ) {
            DropdownMenuItem(onClick = { /* Handle refresh! */ }) {
                Text("item")
            }
            DropdownMenuItem(onClick = {
                viewModel.getUserDataVM {
                    mainActivity.setContent {
                        ChangeDataUser(viewModel = viewModel)
                    }

                }
            }) {
                Text("Изменить анкету")
            }
            Divider()
            DropdownMenuItem(onClick = {
                setStateScreen(mainActivity, 16)
                AUTH.signOut()
                CURRENT_UID = AUTH.currentUser?.uid.toString()
                mainActivity.setContent { ChangeScreen(viewModel = viewModel) }
            }) {
                Text(stringResource(id = R.string.exit_message))
            }
        }
    }
}