package com.asp424.tennis.screens.admin

import android.os.Build
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.asp424.tennis.AUTH
import com.asp424.tennis.MainActivity
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.screens.CellOutField
import com.asp424.tennis.screens.RoundButton
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.utils.setStateScreen
import com.asp424.tennis.utils.showToast

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ScreenEnterCodeAdmin(
    viewModel: MainViewModel
) {
    val mainActivity = LocalContext.current as MainActivity
    setStateScreen(mainActivity = mainActivity, 11)
    val code: String? by viewModel.adminCode.observeAsState("")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = "Введите код")
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CellOutField(
                value = code,
                text = "Введите код администратора",
                keyboard = KeyboardType.Phone,
                onValueChange = { new ->
                    viewModel.setAdminCode(new)
                })
        }
    }
    RoundButton(icon = Icons.Default.Forward, action = {
        if (AUTH.currentUser?.uid != null) {
            viewModel.getCodeFromDB {
                if (it == viewModel.getAdminCode()) {
                    mainActivity.setContent {
                        ScreenAdmin(viewModel = viewModel, id = "")
                    }
                } else {
                    viewModel.setAdminCode("")
                    showToast("Код неверен", mainActivity)
                }
            }
        }
    })
}