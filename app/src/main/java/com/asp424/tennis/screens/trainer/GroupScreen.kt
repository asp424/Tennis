package com.asp424.tennis.screens.trainer

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.asp424.tennis.Blue300
import com.asp424.tennis.MainActivity
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.room.User
import com.asp424.tennis.screens.CellGroup
import com.asp424.tennis.screens.RoundButton
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.utils.appData
import com.asp424.tennis.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun GroupScreen(
    viewModel: MainViewModel,
    navControllerTrainer: NavHostController,
    mainActivity: MainActivity
) {
    val db = LocalContext.current.appData
    val listGroupsVM: MutableList<User>? by viewModel.listClients.observeAsState()
    Column(
        modifier = Modifier.fillMaxSize().background(if (isSystemInDarkTheme()) Color.Black else Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val listRed = mutableListOf<User>()
        val listBlue = mutableListOf<User>()
        val listGreen = mutableListOf<User>()
        val listYellow = mutableListOf<User>()
        TopBar(tittle = "Ваши группы")
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            listGroupsVM?.forEach {
                if (it.color.toString() == "1") listRed.add(it)
                if (it.color.toString() == "2") listBlue.add(it)
                if (it.color.toString() == "3") listGreen.add(it)
                if (it.color.toString() == "4") listYellow.add(it)
            }
            CellGroup(list = listRed, colorBack = Red, textColor = White, viewModel, navControllerTrainer)
            CellGroup(
                list = listBlue,
                colorBack = Blue300,
                textColor = White,
                viewModel = viewModel,
                navControllerTrainer = navControllerTrainer
            )
            CellGroup(
                list = listGreen,
                colorBack = Green,
                textColor = Black,
                viewModel = viewModel,
                navControllerTrainer = navControllerTrainer
            )
            CellGroup(
                list = listYellow,
                colorBack = Yellow,
                textColor = Black,
                viewModel = viewModel,
                navControllerTrainer = navControllerTrainer
            )
        }

    }
    RoundButton(icon = Icons.Default.Add) {
        val list = mutableListOf<User>()
        val listColor = mutableListOf(
            Red,
            Blue300,
            Green,
            Yellow,
        )
        viewModel.listUsersForCreateGroup.postValue(list)
        var counter = 0
        CoroutineScope(Dispatchers.IO).launch {
            db.getAllUsers().forEach {
                counter ++
                listColor.remove(
                    when (it.color) {
                        "1" -> Red
                        "2" -> Blue300
                        "3" -> Green
                        "4" -> Yellow
                        else -> White
                    }
                )
                if (it.color == "0" || it.color == "")
                    list.add(it)
                if (counter == db.getAllUsers().size)
                {
                    viewModel.listOfColors.postValue(listColor)
                    if (list.isNotEmpty())
                    {
                        withContext(Dispatchers.Main) {
                            viewModel.initialCheckListMap()
                            viewModel.checkState.postValue(hashMapOf())
                            viewModel.setGroupName("")
                            viewModel.setNotifyName("Группа")
                            viewModel.listUsersForCreateGroup.postValue(list)
                            navControllerTrainer.navigate("Создание группы"){
                                    popUpTo("Группы"){inclusive = true}
                                }

                        }
                    }
                    else   withContext(Dispatchers.Main) {
                        showToast("Все спортсмены распределены по группам", mainActivity)
                    }
                }
            }
        }
    }
    DropdownTrainer(mainActivity, viewModel)
}