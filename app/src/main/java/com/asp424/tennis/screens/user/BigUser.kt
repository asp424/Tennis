package com.asp424.tennis.screens.user

import android.os.Build
import android.util.Log
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
import com.asp424.tennis.utils.AppValueEventListener
import com.asp424.tennis.utils.getUserModel
import com.asp424.tennis.utils.setStateScreen
import com.asp424.tennis.utils.showToast


@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun EnterDataBigUser(
    viewModel: MainViewModel
) {
    val mainActivity = LocalContext.current as MainActivity
    setStateScreen(mainActivity = mainActivity, 22)
    val usernamePar: String? by viewModel.usernamePar.observeAsState("")

    val secondNameUserPar: String? by viewModel.secondNameUserPar.observeAsState("")

    val patronymicParUser: String? by viewModel.patronymicPar.observeAsState("")
    val eMail: String? by viewModel.eMail.observeAsState("")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
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
                CellTittle(text = "Введите электронную почту")
                OutlinedTextField(
                    value = eMail!!,
                    onValueChange = { new ->
                        viewModel.setEMail(new)
                    }, label = {
                        Text(text = stringResource(id = R.string.e_mail))
                    }, singleLine = true
                )

                CellTittle(text = "Город, в котором будете заниматься")
                MenuCityEnter(viewModel = viewModel)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp, end = 60.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        OutlinedButton(
            onClick = {
                CURRENT_UID = AUTH.currentUser?.uid.toString()
                viewModel.apply {
                    val key = REF_DATABASE_ROOT.push().key
                    if (getUsernamePar().isNotEmpty() && getSecondNameUserPar().isNotEmpty() && getTrainUserCity().isNotEmpty()
                         && getPatronymicPar().isNotEmpty() && getEMail().isNotEmpty()
                    ) {
                        val dateMap = mutableMapOf<String, Any>()
                        dateMap[CHILD_NAME] = getUsernamePar()
                        dateMap[CHILD_SECONDNAME] = getSecondNameUserPar()
                        dateMap[CHILD_PATRONYMIC] = getPatronymicPar()
                        dateMap[CHILD_CITY] = getTrainUserCity()
                        dateMap[CHILD_EMAIL] = getEMail()
                        dateMap[KEY] = key.toString()
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

