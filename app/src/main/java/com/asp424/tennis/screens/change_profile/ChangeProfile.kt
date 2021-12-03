package com.asp424.tennis.screens.change_profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asp424.tennis.MainActivity
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.R
import com.asp424.tennis.permissions.GPS
import com.asp424.tennis.permissions.PERMISSION_REQUEST
import com.asp424.tennis.permissions.checkPermissions
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.screens.admin.EnterDataAdmin
import com.asp424.tennis.screens.admin.ScreenAdmin
import com.asp424.tennis.screens.admin.ScreenEnterCodeAdmin
import com.asp424.tennis.screens.registration.OnCodeSentScreen
import com.asp424.tennis.screens.registration.RegScreen
import com.asp424.tennis.screens.trainer.EnterDataTrainer
import com.asp424.tennis.screens.trainer.TrainerScreen
import com.asp424.tennis.screens.user.EnterDataBigUser
import com.asp424.tennis.screens.user.EnterDataUser
import com.asp424.tennis.screens.user.UserScreen
import com.asp424.tennis.utils.setStateScreen


@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ChangeScreen(
    viewModel: MainViewModel
) {
    viewModel.setValuePhone("")
    viewModel.setCode("")
    val mainActivity = LocalContext.current as MainActivity
    val navController: NavHostController = rememberNavController()
    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        TopBar(tittle = stringResource(id = R.string.change_profile))
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clickable {
                        setStateScreen(mainActivity = mainActivity, 1)
                        checkPermissions(GPS, mainActivity, reqCode = PERMISSION_REQUEST)
                        navController.navigate(mainActivity.resources.getString(R.string.enter_phone_nav))
                    }, elevation = 10.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.trainer),
                        Modifier.padding(8.dp),
                        style = TextStyle(fontSize = 16.sp, color = Color.Black)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .clickable {
                        setStateScreen(mainActivity = mainActivity, 2)
                        navController.navigate(mainActivity.resources.getString(R.string.enter_phone_nav))
                    }, elevation = 10.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.user),
                        Modifier.padding(8.dp),
                        style = TextStyle(fontSize = 16.sp, color = Color.Black)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .clickable {
                        setStateScreen(mainActivity = mainActivity, 33)
                        navController.navigate(mainActivity.resources.getString(R.string.enter_phone_nav))
                    }, elevation = 10.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.userBig),
                        Modifier.padding(8.dp),
                        style = TextStyle(fontSize = 16.sp, color = Color.Black)
                    )
                }
            }
            val count = remember { mutableStateOf(0) }
            if (count.value == 10) {
                count.value = 0
                setStateScreen(mainActivity = mainActivity, 10)
                navController.navigate("Ввод номера")
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable {
                    count.value++

                }) {
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = mainActivity.resources.getString(R.string.profile_nav)
    ) {
        composable(mainActivity.resources.getString(R.string.profile_nav)) {
        }
        composable("Ученик") {
            UserScreen(viewModel = viewModel, id = "")
        }
        composable("Тренер") {
            TrainerScreen(viewModel = viewModel, id = "")
        }
        composable("Код админ") {
            ScreenEnterCodeAdmin(viewModel = viewModel)
        }
        composable(mainActivity.resources.getString(R.string.admin_nav)) {
            ScreenAdmin(viewModel, "")
        }
        composable(mainActivity.resources.getString(R.string.trainer_data_nav)) {
            TopBar(tittle = stringResource(id = R.string.enter_you_data))
            EnterDataTrainer(viewModel = viewModel)
        }
        composable(mainActivity.resources.getString(R.string.user_data_nav)) {
            TopBar(tittle = stringResource(id = R.string.enter_you_data))
            EnterDataUser(viewModel = viewModel)
        }
        composable("Взрослый") {
            TopBar(tittle = stringResource(id = R.string.enter_you_data))
            EnterDataBigUser(viewModel = viewModel)
        }
        composable("Ввод данных админа") {
            TopBar(tittle = stringResource(id = R.string.enter_you_data))
            EnterDataAdmin(viewModel = viewModel)
        }
        composable(mainActivity.resources.getString(R.string.enter_phone_nav)) {
            RegScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(mainActivity.resources.getString(R.string.enter_code_nav)) {
            OnCodeSentScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}

