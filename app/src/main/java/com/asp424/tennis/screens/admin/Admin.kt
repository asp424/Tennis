package com.asp424.tennis.screens.admin

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asp424.tennis.*
import com.asp424.tennis.R
import com.asp424.tennis.screens.NotifyScreen
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.screens.change_profile.ChangeScreen
import com.asp424.tennis.screens.trainer.*
import com.asp424.tennis.utils.appData
import com.asp424.tennis.utils.setStateScreen
import com.asp424.tennis.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ScreenAdmin(
    viewModel: MainViewModel,
    id: String
) {
    var notif = id
    val mainActivity = LocalContext.current as MainActivity
    val db = LocalContext.current.appData
    var rasp by remember {
        mutableStateOf(true)
    }
    var trainers by remember {
        mutableStateOf(false)
    }
    var users by remember {
        mutableStateOf(false)
    }
    var groups by remember {
        mutableStateOf(false)
    }
    setStateScreen(mainActivity = mainActivity, 12)
    CoroutineScope(Dispatchers.IO).launch {
        viewModel.setListTrainers(db.getAllTrainers())
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val navControllerAdmin = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigation(backgroundColor = if (isSystemInDarkTheme()) Blue300 else Blue200) {
                    BottomNavigationItem(
                        selected = rasp,
                        onClick = {
                            rasp = true
                            trainers = false
                            users = false
                            groups = false
                            navControllerAdmin.navigate("????????????????????") {
                                popUpTo("????????????????????") { inclusive = true }
                            }
                        },
                        label = { Text(text = "????????????????????", style = TextStyle(fontSize = 10.sp)) },
                        icon = {
                            Icon(Icons.Default.Schedule, "ass")
                        }

                    )
                    BottomNavigationItem(
                        selected = users,
                        onClick = {
                            rasp = false
                            trainers = false
                            users = true
                            groups = false
                            navControllerAdmin.navigate("????????????????????") {
                                popUpTo("????????????????????") { inclusive = true }
                            }
                        },
                        label = { Text(text = "????????????????????", style = TextStyle(fontSize = 10.sp)) },
                        icon = {
                            Icon(Icons.Default.PeopleAlt, "ass")
                        }

                    )
                    BottomNavigationItem(
                        selected = groups,
                        onClick = {
                            rasp = false
                            trainers = false
                            users = false
                            groups = true
                            navControllerAdmin.navigate("????????????") {
                                popUpTo("????????????") { inclusive = true }
                            }
                        },
                        label = { Text(text = "????????????") },
                        icon = {
                            Icon(Icons.Default.Groups, "ass")
                        }

                    )
                    BottomNavigationItem(
                        selected = trainers,
                        onClick = {
                            rasp = false
                            trainers = true
                            users = false
                            groups = false
                            navControllerAdmin.navigate("??????????????") {
                                popUpTo("??????????????") { inclusive = true }
                            }
                        },
                        label = { Text(text = "??????????????") },
                        icon = {
                            Icon(Icons.Default.SportsTennis, "ass")
                        }
                    )
                }
            }
        ) {
            DropdownAdmin(mainActivity = mainActivity, viewModel = viewModel)
            NavHost(navController = navControllerAdmin, startDestination = "????????????????????") {
                composable("??????????????") {
                    DrawTrainersScreen(
                        mainActivity = mainActivity,
                        navControllerAdmin = navControllerAdmin,
                        viewModel = viewModel
                    )
                    viewModel.getListTrainers()
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.setListTrainers(db.getAllTrainers())
                    }
                }
                composable("????????????????????") {
                    DrawClientsScreen(
                        mainActivity = mainActivity,
                        navControllerTrainer = navControllerAdmin,
                        viewModel = viewModel
                    )
                    viewModel.getListClientsTrainer()
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.setListClients(db.getAllUsers())
                    }
                }
                composable("???????? ????????????????") {
                    DatePicker(mainActivity, viewModel, navControllerAdmin)
                }
                composable("??????") {
                    Chat(viewModel)
                }
                composable("?????????????? ?? ????") {
                    ClientsInBDScreen(
                        viewModel = viewModel,
                        navControllerAdmin
                    )
                }
                composable("?????????? ????????????") {
                    StartTime(viewModel, navControllerAdmin, mainActivity)
                }
                composable("?????????? ???????????? ????????????") {
                    StartTimeGroup(viewModel, navControllerAdmin, mainActivity)
                }
                composable("?????????? ??????????????????") {
                    EndTime(viewModel, navControllerAdmin, mainActivity)
                }
                composable("?????????? ?????????????????? ????????????") {
                    EndTimeGroup(viewModel, navControllerAdmin, mainActivity)
                }
                composable("???????? ????????????") {
                    CalStart(viewModel, navControllerAdmin, mainActivity)

                }
                composable("??????????????????????") {
                    NotifyScreen(viewModel)
                }
                composable("?????????? ????????????") {
                    SelectMethod(navControllerAdmin, viewModel)
                }
                composable("????????????????????") {
                    if (notif.isNotEmpty()) {
                        viewModel.getUserName(id = notif) {
                            viewModel.setNotifyName(it)
                            viewModel.setNotifyId(notif)
                            notif = ""
                            navControllerAdmin.navigate("??????")
                        }
                    }
                    else {
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                        viewModel.getListOfEvents {
                            if (viewModel.eventList.value.isNullOrEmpty()) {
                                showToast("???????????????? ???????????????? ?? ????????????????????", mainActivity)
                                navControllerAdmin.navigate("????????????????????")
                            } else
                                navControllerAdmin.navigate("Set1") {
                                    popUpTo("Set1") { inclusive = true }
                                }
                        }
                    }
                }
                composable("???????? ???????????? ????????????") {
                    CalStartGroup(viewModel, navControllerAdmin, mainActivity)
                }
                composable("???????????????????? ?? ????????????????????") {
                    ScreenAddToSchedule(viewModel, mainActivity, navControllerAdmin)
                }
                composable("???????????????????? ?? ???????????????????? ????????????") {
                    ScreenAddToScheduleGroup(viewModel, mainActivity, navControllerAdmin)
                }
                composable("???????????????????????????? ????????????") {
                    EditGroup(viewModel, navControllerAdmin, mainActivity)
                }
                composable("???????????????? ??????????????") {
                    CreateNewClient(
                        mainActivity = mainActivity,
                        viewModel = viewModel,
                        navControllerAdmin
                    )
                }
                composable("???????????????? ????????????") {
                    viewModel.getListOfColors{
                        navControllerAdmin.navigate("God")
                    }
                }
                composable("God") {
                    if (viewModel.listOfColors.value.isNullOrEmpty())
                    {
                        showToast("?????? ???????????? ??????????????", mainActivity)
                    }
                    else CreateGroup(
                        viewModel,
                        navControllerAdmin,
                        mainActivity,
                        viewModel.listOfColors.value!!,
                        viewModel.listUsersForCreateGroup.value!!
                    )
                }
                composable("????????????") {
                        GroupScreen(viewModel, navControllerAdmin, mainActivity)
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.setListClients(db.getAllUsers())
                        }
                        viewModel.getListClientsTrainer()
                    }

                    composable("???????????????????? ??????????????") {
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                        if (viewModel.eventList.value.isNullOrEmpty()) {
                            showToast("?? ???????????????????? ?????????????? ???????? ?????? ??????????????", mainActivity)
                            navControllerAdmin.navigate("??????????????")
                        } else
                            navControllerAdmin.navigate("Set") {
                                popUpTo("Set") { inclusive = true }
                            }

                    }
                    composable("Set") {
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            TopBar(tittle = viewModel.getNotifyName())
                            var scale by remember { mutableStateOf(1f) }
                            val state = rememberTransformableState { zoomChange, _, _ ->
                                scale *= zoomChange

                            }
                            WeekScheduleTheme {
                                Surface(color = MaterialTheme.colors.background) {
                                    viewModel.eventList.value?.let { it1 ->
                                        Schedule(
                                            state = state,
                                            scale = scale,
                                            events = it1,
                                            viewModel
                                        )
                                    }
                                }
                            }
                        }
                    }
                composable("Set1") {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        var scale by remember { mutableStateOf(1f) }
                        val state = rememberTransformableState { zoomChange, _, _ ->
                            scale *= zoomChange

                        }
                        WeekScheduleTheme {
                            Surface(color = MaterialTheme.colors.background) {
                                viewModel.eventList.value?.let { it1 ->
                                    Schedule(
                                        state = state,
                                        scale = scale,
                                        events = it1,
                                        viewModel
                                    )
                                }
                            }
                        }
                    }
                }
                    composable("????????????????????") {
                        Coordinates(viewModel)
                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @Composable
    fun DropdownAdmin(mainActivity: MainActivity, viewModel: MainViewModel) {
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
                    tint = Color.White,
                    contentDescription = stringResource(id = R.string.content_desc)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = { /* Handle refresh! */ }) {
                    Text("item")
                }
                DropdownMenuItem(onClick = { /* Handle settings! */ }) {
                    Text("item")
                }
                /*Divider()*/
                DropdownMenuItem(onClick = {
                    setStateScreen(mainActivity, 16)
                    AUTH.signOut()
                    CURRENT_UID = AUTH.currentUser?.uid.toString()
                    mainActivity.setContent {
                        ChangeScreen(
                            viewModel = viewModel
                        )
                    }
                }) {
                    Text("?????????? ???? ????????????????")
                }
            }
        }

    }