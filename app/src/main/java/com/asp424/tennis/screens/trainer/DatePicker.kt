package com.asp424.tennis.screens.trainer

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.asp424.tennis.MainActivity
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.R
import com.asp424.tennis.screens.RoundButton
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.screens.user.EnterDataUser
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UseCompatLoadingForDrawables", "SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    mainActivity: MainActivity,
    viewModel: MainViewModel,
    navControllerTrainer: NavHostController
) {

        Column(Modifier.fillMaxSize().background(if (isSystemInDarkTheme()) Color.Black else Color.White), horizontalAlignment = Alignment.CenterHorizontally) {
            TopBar(tittle = "Выберите день рождения")
            AndroidView(
                { android.widget.DatePicker(mainActivity, null, R.style.DatePickerSpinnerStyle) },
                update = { views ->
                    val calendar = Calendar.getInstance()
                    views.spinnersShown = true
                    views.calendarViewShown = false
                    views.init(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        android.widget.DatePicker.OnDateChangedListener {_, i, i2, i3 ->
                            val years = SimpleDateFormat("yyyy").format(Date()).toInt() - i
                            viewModel.yearsChild.value = years.toString()
                            val sum = i2 + 1
                            if (sum.toString().length == 1)
                                viewModel.setYearsOldChild("$i3-0$sum-$i")
                            else viewModel.setYearsOldChild("$i3-$sum-$i")

                        })
                }, modifier = Modifier.padding(top = 60.dp)
            )
        }
        RoundButton(icon = Icons.Default.ArrowForward) {
            navControllerTrainer.navigate("Создание клиента")
        }
}


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@SuppressLint("UseCompatLoadingForDrawables", "SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerUser(
    mainActivity: MainActivity,
    viewModel: MainViewModel
) {

    Column(Modifier.fillMaxSize().background(if (isSystemInDarkTheme()) Color.Black else Color.White),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TopBar(tittle = "Выберите день рождения")
        AndroidView(
            { android.widget.DatePicker(mainActivity, null, R.style.DatePickerSpinnerStyle) },
            update = { views ->
                val calendar = Calendar.getInstance()
                views.spinnersShown = true
                views.calendarViewShown = false
                views.init(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    android.widget.DatePicker.OnDateChangedListener {_, i, i2, i3 ->
                        var day = i3.toString()
                        if (day.length == 1)
                            day = "0$day"
                        val years = SimpleDateFormat("yyyy").format(Date()).toInt() - i
                        viewModel.yearsChild.value = years.toString()
                        val sum = i2 + 1
                        if (sum.toString().length == 1)
                            viewModel.setYearsOldChild("$day-0$sum-$i")
                        else viewModel.setYearsOldChild("$day-$sum-$i")

                    })
                     }, modifier = Modifier.padding(top = 60.dp)
        )
    }
    RoundButton(icon = Icons.Default.ArrowForward) {
        mainActivity.setContent {
            EnterDataUser(viewModel = viewModel)
        }
    }
}