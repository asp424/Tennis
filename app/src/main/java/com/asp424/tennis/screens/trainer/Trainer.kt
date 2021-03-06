package com.asp424.tennis.screens.trainer

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.os.Build
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
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
import com.asp424.tennis.screens.change_profile.ChangeScreen
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
fun TrainerScreen(
    viewModel: MainViewModel,
    id: String
) {
    var notif = id
    val mainActivity = LocalContext.current as MainActivity
    val db = LocalContext.current.appData
    CoroutineScope(Dispatchers.IO).launch {
        viewModel.setListClients(db.getAllUsers())
    }
    setStateScreen(mainActivity = mainActivity, 5)
    val navControllerTrainer = rememberNavController()
    var rasp by remember {
        mutableStateOf(true)
    }
    var stud by remember {
        mutableStateOf(false)
    }
    var group by remember {
        mutableStateOf(false)
    }
    var clients by remember {
        mutableStateOf(false)
    }
    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = if (isSystemInDarkTheme()) Blue300 else Blue200) {
                BottomNavigationItem(
                    selected = rasp,
                    onClick = {
                        rasp = true
                        stud = false
                        group = false
                        clients = false
                        navControllerTrainer.navigate("????????????????????") {
                            popUpTo("????????????????????") { inclusive = true }
                        }
                    },
                    label = { Text(text = "????????????????????", style = TextStyle(fontSize = 10.sp)) },
                    icon = {
                        Icon(Icons.Default.Schedule, "ass")
                    }
                )
                BottomNavigationItem(
                    selected = stud,
                    onClick = {
                        rasp = false
                        stud = true
                        group = false
                        clients = false
                        navControllerTrainer.navigate("????????????????????") {
                            popUpTo("????????????????????") { inclusive = true }
                        }
                    },
                    label = { Text(text = "????????????????????", style = TextStyle(fontSize = 10.sp)) },
                    icon = {
                        Icon(Icons.Default.PeopleAlt, "ass")
                    }, modifier = Modifier
                        .fillMaxSize()
                )
                BottomNavigationItem(
                    selected = clients,
                    onClick = {
                        rasp = false
                        stud = true
                        group = false
                        clients = true
                        navControllerTrainer.navigate("??????????????") {
                            popUpTo("??????????????") { inclusive = true }
                        }
                    },
                    label = { Text(text = "??????????????", style = TextStyle(fontSize = 10.sp)) },
                    icon = {
                        Icon(Icons.Default.PeopleAlt, "ass")
                    }, modifier = Modifier
                        .fillMaxSize()
                )
                BottomNavigationItem(
                    selected = group,
                    onClick = {
                        rasp = false
                        stud = false
                        group = true
                        clients = false
                        navControllerTrainer.navigate("????????????") {
                            popUpTo("????????????") { inclusive = true }
                        }
                    },
                    label = { Text(text = "????????????") },
                    icon = {
                        Icon(Icons.Default.Groups, "ass")
                    }, modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    ) {
        DropdownTrainer(mainActivity = mainActivity, viewModel)
        NavHost(navController = navControllerTrainer, startDestination = "????????????????????") {
            composable("????????????????????") {
                if (notif.isNotEmpty()) {
                    viewModel.getUserName(id = notif) {
                        viewModel.setNotifyName(it)
                        viewModel.setNotifyId(notif)
                        notif = ""
                        navControllerTrainer.navigate("??????")
                    }
                } else {
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
                            navControllerTrainer.navigate("????????????????????")
                        } else
                            navControllerTrainer.navigate("Set") {
                                popUpTo("????????????????????") { inclusive = true }
                            }
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
            composable("????????????????????") {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.setListClients(db.getAllUsers())
                }
                DrawClientsScreen(
                    mainActivity = mainActivity,
                    navControllerTrainer = navControllerTrainer,
                    viewModel = viewModel
                )
                viewModel.getListClientsTrainer()
            }

            composable("??????????????") {
                DrawClientsScreenBig(
                    mainActivity = mainActivity, navControllerTrainer = navControllerTrainer,
                    viewModel = viewModel
                )
                viewModel.getListClientsTrainerBig()
            }
            composable("??????????????????????") {
                NotifyScreen(viewModel)
            }
            composable("??????") {
                Chat(viewModel)
            }
            composable("???????? ????????????????") {
                DatePicker(mainActivity, viewModel, navControllerTrainer)
            }
            composable("????????????") {
                GroupScreen(viewModel, navControllerTrainer, mainActivity)
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.setListClients(db.getAllUsers())
                }
                viewModel.getListClientsTrainer()
            }
            composable("?????????? ????????????") {
                SelectMethod(navControllerTrainer, viewModel)
            }

            composable("?????????? ???????????? Big") {
                SelectMethodBig(navControllerTrainer, viewModel)
            }
            composable("?????????????? ?? ????") {
                ClientsInBDScreen(
                    viewModel = viewModel,
                    navControllerTrainer
                )
            }
            composable("?????????????? ?? ???? Big") {
                ClientsInBDScreenBig(
                    viewModel = viewModel,
                    navControllerTrainer
                )
            }

            composable("?????????? ????????????") {
                StartTime(viewModel, navControllerTrainer, mainActivity)
            }
            composable("?????????? ???????????? ????????????") {
                StartTimeGroup(viewModel, navControllerTrainer, mainActivity)
            }
            composable("?????????? ??????????????????") {
                EndTime(viewModel, navControllerTrainer, mainActivity)
            }
            composable("?????????? ?????????????????? ????????????") {
                EndTimeGroup(viewModel, navControllerTrainer, mainActivity)
            }
            composable("???????? ????????????") {
                CalStart(viewModel, navControllerTrainer, mainActivity)
            }
            composable("???????? ???????????? ????????????") {
                CalStartGroup(viewModel, navControllerTrainer, mainActivity)
            }
            composable("???????????????????? ?? ????????????????????") {
                ScreenAddToSchedule(viewModel, mainActivity, navControllerTrainer)
            }
            composable("???????????????????? ?? ???????????????????? ????????????") {
                ScreenAddToScheduleGroup(viewModel, mainActivity, navControllerTrainer)
            }
            composable("???????????????????????????? ????????????") {
                EditGroup(viewModel, navControllerTrainer, mainActivity)
            }
            composable("???????????????? ??????????????") {
                CreateNewClient(
                    mainActivity = mainActivity,
                    viewModel = viewModel,
                    navControllerTrainer
                )
            }
            composable("???????????????? ????????????") {
                if (viewModel.listOfColors.value.isNullOrEmpty()) {
                    showToast("?????? ???????????? ??????????????", mainActivity)

                } else CreateGroup(
                    viewModel, navControllerTrainer, mainActivity, viewModel.listOfColors.value!!,
                    viewModel.listUsersForCreateGroup.value!!
                )
            }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun DropdownTrainer(mainActivity: MainActivity, viewModel: MainViewModel) {
    val db = LocalContext.current.appData
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
                CoroutineScope(Dispatchers.IO).launch {
                    db.getAllUsers().forEach {
                        db.deleteUser(it)
                    }
                }
                setStateScreen(mainActivity, 16)
                val scheduler =
                    mainActivity.getSystemService(JobService.JOB_SCHEDULER_SERVICE) as JobScheduler
                scheduler.cancelAll()
                AUTH.signOut()
                CURRENT_UID = AUTH.currentUser?.uid.toString()
                mainActivity.setContent { ChangeScreen(viewModel = viewModel) }
            }) {
                Text("?????????? ???? ????????????????")
            }
        }
    }

}

fun scheduleJob(mainActivity: MainActivity) {
    val componentName = ComponentName(mainActivity, MyJobScheduler::class.java)
    val info = JobInfo.Builder(123, componentName)
        .setRequiresCharging(false)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .setPersisted(true)
        .setPeriodic((15 * 60 * 1000L))
        .build()
    val scheduler =
        mainActivity.getSystemService(JobService.JOB_SCHEDULER_SERVICE) as JobScheduler
    if (scheduler.allPendingJobs.size == 0)
        scheduler.schedule(info)
}
