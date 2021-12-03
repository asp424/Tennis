package com.asp424.tennis.screens.admin

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.asp424.tennis.*
import com.asp424.tennis.R
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.screens.trainer.TrainerScreen
import com.asp424.tennis.utils.setStateScreen
import com.asp424.tennis.utils.showToast

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun EnterDataAdmin(
    viewModel: MainViewModel
) {
    val mainActivity = LocalContext.current as MainActivity
    setStateScreen(mainActivity = mainActivity, 3)
    val name: String? by viewModel.name.observeAsState("")
    val secondName: String? by viewModel.secondName.observeAsState("")
    val patronymic: String? by viewModel.patronymic.observeAsState("")
    val workPlace: String? by viewModel.workPlace.observeAsState("")
    val eMail: String? by viewModel.eMail.observeAsState("")
    val whatsapp: String? by viewModel.whatsapp.observeAsState("")
    val isValidEmail = viewModel.getEMail()
        .count() > 5 && '@' in viewModel.getEMail() && '.' in viewModel.getEMail()
    val isValidName = viewModel.getName().count() > 1
    val isValidSecondName = viewModel.getSecondName().count() > 1
    val isValidPatronymic = viewModel.getPatronymic().count() > 1
    val isValidWorkPlace = viewModel.getWorkPlace().count() > 1
    val isValidWhatsApp = if ('+' in viewModel.getWhatsapp()) viewModel.getWhatsapp().count() == 12
    else viewModel.getWhatsapp().count() == 11
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = stringResource(id = R.string.enter_you_data))
        Box(Modifier.padding(top = 8.dp)) {
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    isError = !isValidName,
                    value = name!!,
                    onValueChange = { new ->
                        viewModel.setName(new)
                    }, label = {
                        Text(text = stringResource(id = R.string.name))
                    }, singleLine = true
                )

                OutlinedTextField(
                    isError = !isValidSecondName,
                    value = secondName!!,
                    onValueChange = { new ->
                        viewModel.setSecondName(new)
                    }, label = {
                        Text(text = stringResource(id = R.string.second_name))
                    }, singleLine = true
                )
                OutlinedTextField(
                    isError = !isValidPatronymic,
                    value = patronymic!!,
                    onValueChange = { new ->
                        viewModel.setPatronymic(new)
                    }, label = {
                        Text(text = stringResource(id = R.string.patronymic))
                    }, singleLine = true
                )
                OutlinedTextField(
                    isError = !isValidWorkPlace,
                    value = workPlace!!,
                    onValueChange = { new ->
                        viewModel.setWorkPlace(new)
                    }, label = {
                        Text(text = stringResource(id = R.string.work_place))
                    }, singleLine = true
                )

                OutlinedTextField(
                    isError = !isValidEmail,
                    value = eMail!!,
                    onValueChange = { new ->
                        viewModel.setEMail(new)
                    },
                    label = {
                        Text(text = stringResource(id = R.string.e_mail))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                OutlinedTextField(
                    isError = !isValidWhatsApp,
                    value = whatsapp!!,
                    onValueChange = { new ->
                        viewModel.setWhatsapp(new)
                    }, label = {
                        Text(text = stringResource(id = R.string.whatsapp))
                    }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Box(modifier = Modifier.padding(start = 230.dp, top = 80.dp, bottom = 40.dp)) {
                    OutlinedButton(
                        onClick = {
                            viewModel.apply {
                                CURRENT_UID = AUTH.currentUser?.uid.toString()
                                if (isValidName && isValidSecondName
                                    && isValidPatronymic && isValidWorkPlace
                                    && isValidEmail && isValidWhatsApp
                                ) {
                                    val dateMap = mutableMapOf<String, Any>()
                                    dateMap[CHILD_NAME] = getName()
                                    dateMap[CHILD_SECONDNAME] = getSecondName()
                                    dateMap[CHILD_PATRONYMIC] = getPatronymic()
                                    dateMap[CHILD_WORK_PLACE] = getWorkPlace()
                                    dateMap[CHILD_EMAIL] = getEMail()
                                    dateMap[CHILD_WHATSAPP] = getWhatsapp()
                                    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID)
                                        .updateChildren(
                                            dateMap
                                        ).addOnCompleteListener {
                                            mainActivity.setContent {
                                                ScreenAdmin(
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
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color.Gray),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = stringResource(id = R.string.content_desc)
                        )
                    }
                }
            }
        }
    }
}