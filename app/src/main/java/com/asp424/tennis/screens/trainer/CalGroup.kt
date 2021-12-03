package com.asp424.tennis.screens.trainer

import android.annotation.SuppressLint
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.asp424.tennis.MainActivity
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.R
import com.asp424.tennis.screens.RoundButton
import com.asp424.tennis.screens.TopBar
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.*


@SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
@Composable
fun CalStartGroup(
    viewModel: MainViewModel,
    navControllerTrainer: NavHostController,
    mainActivity: MainActivity
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
    ) {
        TopBar(tittle = "Выберите дату занятия")
        AndroidView(
            {
                MaterialCalendarView(ContextThemeWrapper(mainActivity, R.style.CalenderViewCustom))
            },
            modifier = Modifier.fillMaxSize(),
            update = { views ->
                viewModel.getDateStart().forEach {
                    views.setDateSelected(CalendarDay.from(it.substring(0, 4).toInt(), it.substring(5, 7).toInt(), it.substring(8, 10).toInt()), true)
                }
                views.state().edit()
                    .setMinimumDate(CalendarDay.from(2021, 9, 16))
                    .commit()
                views.selectionMode = MaterialCalendarView.SELECTION_MODE_MULTIPLE
                views.setOnDateChangedListener { _, date, selected ->
                    var month = date.day.toString()
                    if (month.length == 1) {
                        month = "0$month"
                    }
                    val m = date.month
                    if (m.toString().length == 1)
                    {
                        if (selected)
                            viewModel._dateStart.value?.add("${date.year}-0${m}-$month")
                        else viewModel._dateStart.value?.remove("${date.year}-0${m}-$month")
                    }
                    else {
                        if (selected)
                            viewModel._dateStart.value?.add("${date.year}-$m-$month")
                        else viewModel._dateStart.value?.remove("${date.year}-0${m}-$month")
                    }
                }
            }
        )
    }
    RoundButton(icon = Icons.Default.ArrowForward) {
        navControllerTrainer.navigate("Добавление в расписание группы")
    }
}


@Composable
fun StartTimeGroup(
    viewModel: MainViewModel,
    navControllerTrainer: NavHostController,
    mainActivity: MainActivity
) {
    Column(Modifier.fillMaxSize().background(if (isSystemInDarkTheme()) Color.Black else Color.White)) {
        TopBar(tittle = "Выберите время начала занятий")
        AndroidView({ TimePicker(ContextThemeWrapper(mainActivity, R.style.TimeTheme)) },
            Modifier.wrapContentSize(),
            update = { view ->
                view.setOnTimeChangedListener { _, hour, min ->
                    var hourNorm = hour.toString()
                    if (hourNorm.length == 1)
                        hourNorm = "0$hourNorm"
                    if (min.toString().length == 1)
                        viewModel.setTimeStart("$hourNorm:0$min:00")
                    else viewModel.setTimeStart("$hourNorm:$min:00")
                }
                view.setIs24HourView(true)
            }
        )
    }
    RoundButton(icon = Icons.Default.ArrowForward) {
        navControllerTrainer.navigate("Добавление в расписание группы")
    }
}

@Composable
fun EndTimeGroup(
    viewModel: MainViewModel,
    navControllerTrainer: NavHostController,
    mainActivity: MainActivity
) {
    Column(Modifier.fillMaxSize().background(if (isSystemInDarkTheme()) Color.Black else Color.White)) {
        TopBar(tittle = "Выберите время окончания занятий")
        AndroidView({ TimePicker(ContextThemeWrapper(mainActivity, R.style.TimeTheme))},
            Modifier.wrapContentSize(),
            update = { view ->
                view.setOnTimeChangedListener { _, hour, min ->
                    var hourNorm = hour.toString()
                    if (hourNorm.length == 1)
                        hourNorm = "0$hourNorm"
                    if (min.toString().length == 1)
                        viewModel.setTimeEnd("$hourNorm:0$min:00")
                    else viewModel.setTimeEnd("$hourNorm:$min:00")
                }
                view.setIs24HourView(true)
            }
        )
    }
    RoundButton(icon = Icons.Default.ArrowForward) {
        navControllerTrainer.navigate("Добавление в расписание группы")
    }
}