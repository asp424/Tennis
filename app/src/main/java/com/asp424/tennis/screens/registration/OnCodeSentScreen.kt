package com.asp424.tennis.screens.registration

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.asp424.tennis.utils.AppValueEventListener
import com.asp424.tennis.*
import com.asp424.tennis.R
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.utils.getStateScreen
import com.asp424.tennis.utils.getUserModel
import com.asp424.tennis.utils.showToast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.ServerValue

@Composable
fun OnCodeSentScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val mainActivity = LocalContext.current as MainActivity
    val codeReg: String? by viewModel.code.observeAsState("")
    if (viewModel.getCode().length == 6) {
        if (AUTH.currentUser?.uid != null) createNodes(
            viewModel, navController, mainActivity
        )
        else enterCode(
            viewModel.getCode(),
            viewModel,
            navController,
            mainActivity
        )
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(color = White)
    ) {
        TopBar(tittle = stringResource(id = R.string.enter_code))
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.wrapContentSize()) {
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Blue,
                        unfocusedBorderColor = Blue300,
                        cursorColor = Blue,
                        textColor = Blue,
                        unfocusedLabelColor = Blue300,
                        focusedLabelColor = Blue
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    value = codeReg!!,
                    onValueChange = { new ->
                        viewModel.setCode(new)

                    }, label = {
                        /*Row(Modifier.wrapContentSize(), horizontalArrangement = Arrangement.Center) {
                            Text(text = "_ _ _ _ _ _")
                        }*/
                    }, singleLine = true, textStyle = TextStyle(fontSize = 24.sp)
                )
            }

            Text(
                modifier = Modifier.padding(16.dp), text =
                stringResource(id = R.string.code_message),
                style = TextStyle(textAlign = TextAlign.Center)
            )
        }
    }
}

private fun createNodes(
    viewModel: MainViewModel,
    navController: NavHostController,
    mainActivity: MainActivity
) {
    MESSAGING_TOKEN.addOnCompleteListener(OnCompleteListener { ass ->
        if (!ass.isSuccessful) {
            navController.navigate(mainActivity.resources.getString(R.string.enter_phone_nav))
            return@OnCompleteListener
        } else {
            val messagingToken = ass.result.toString()
            val uid = AUTH.currentUser?.uid.toString()
            CURRENT_UID = uid
            when (getStateScreen(mainActivity = mainActivity)) {
                1 -> {
                    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(uid)
                        .addListenerForSingleValueEvent(AppValueEventListener {
                            if (it.getUserModel().name.isNotEmpty()
                            ) {
                                val dateMap = mutableMapOf<String, Any>()
                                dateMap[CHILD_TOKEN] = messagingToken
                                dateMap[CHILD_PHONE] = viewModel.getPhoneNumber()
                                REF_DATABASE_ROOT.child(NODE_TRAINERS).child(uid).updateChildren(
                                    dateMap
                                ).addOnSuccessListener {
                                    navController.navigate("Тренер")
                                }
                            } else {
                                viewModel.clearData()
                                trainersData(
                                uid,
                                viewModel,
                                messagingToken,
                                navController, mainActivity
                            )}
                        })
                }
                2 -> {
                    REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                        .addListenerForSingleValueEvent(AppValueEventListener {
                            if (it.getUserModel().name.isNotEmpty()) {
                                val dateMap = mutableMapOf<String, Any>()
                                dateMap[CHILD_TOKEN] = messagingToken
                                dateMap[CHILD_SCHOOL] = "small"
                                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(
                                    dateMap
                                ).addOnSuccessListener {
                                    navController.navigate("Ученик")
                                }
                            } else usersData(
                                uid,
                                viewModel,
                                messagingToken,
                                navController, mainActivity
                            )
                        })
                }
                10 ->{
                    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(uid)
                        .addListenerForSingleValueEvent(AppValueEventListener {
                            if (it.getUserModel().name.isNotEmpty()
                            ) {
                                val dateMap = mutableMapOf<String, Any>()
                                dateMap[CHILD_TOKEN] = messagingToken
                                dateMap[CHILD_PHONE] = viewModel.getPhoneNumber()
                                REF_DATABASE_ROOT.child(NODE_TRAINERS).child(uid).updateChildren(
                                    dateMap
                                ).addOnSuccessListener {
                                    navController.navigate("Руководитель")
                                }
                            } else {
                                viewModel.clearData()
                                adminData(
                                    uid,
                                    viewModel,
                                    messagingToken,
                                    navController, mainActivity
                                )
                            }
                        })
                }
                33 -> {
                    REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                        .addListenerForSingleValueEvent(AppValueEventListener {
                            if (it.getUserModel().name.isNotEmpty()) {
                                val dateMap = mutableMapOf<String, Any>()
                                dateMap[CHILD_TOKEN] = messagingToken
                                dateMap[CHILD_SCHOOL] = "big"
                                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(
                                    dateMap
                                ).addOnSuccessListener {
                                    navController.navigate("Ученик")
                                }
                            } else usersDataBig(
                                uid,
                                viewModel,
                                messagingToken,
                                navController, mainActivity
                            )
                        })
                }
            }
        }
    })
}

private fun trainersData(
    uid: String,
    viewModel: MainViewModel,
    messagingToken: String,
    navController: NavHostController,
    mainActivity: MainActivity
) {
    val dateMap = mutableMapOf<String, Any>()
    dateMap[CHILD_ID] = uid
    dateMap[CHILD_PHONE] = viewModel.getPhoneNumber()
    dateMap[CHILD_REGISTRATION_TIMESTAMP] = ServerValue.TIMESTAMP
    dateMap[CHILD_TOKEN] = messagingToken
    dateMap[CHILD_SCHOOL] = "big"
    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(uid).updateChildren(
        dateMap
    ).addOnSuccessListener {
        navController.navigate(mainActivity.resources.getString(R.string.trainer_data_nav))
    }
        .addOnFailureListener {
            navController.navigate(mainActivity.resources.getString(R.string.enter_phone_nav))
        }
}
private fun adminData(
    uid: String,
    viewModel: MainViewModel,
    messagingToken: String,
    navController: NavHostController,
    mainActivity: MainActivity
) {
    val dateMap = mutableMapOf<String, Any>()
    dateMap[CHILD_ID] = uid
    dateMap[CHILD_PHONE] = viewModel.getPhoneNumber()
    dateMap[CHILD_REGISTRATION_TIMESTAMP] = ServerValue.TIMESTAMP
    dateMap[CHILD_TOKEN] = messagingToken
    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(uid).updateChildren(
        dateMap
    ).addOnSuccessListener {
        navController.navigate("Ввод данных админа")
    }
        .addOnFailureListener {
            navController.navigate(mainActivity.resources.getString(R.string.enter_phone_nav))
        }
}
private fun usersData(
    uid: String,
    viewModel: MainViewModel,
    messagingToken: String,
    navController: NavHostController,
    mainActivity: MainActivity
) {
    val dateMap = mutableMapOf<String, Any>()
    dateMap[CHILD_ID] = uid
    dateMap[CHILD_PHONE] = viewModel.getPhoneNumber()
    dateMap[CHILD_REGISTRATION_TIMESTAMP] = ServerValue.TIMESTAMP
    dateMap[CHILD_TOKEN] = messagingToken
    dateMap[CHILD_SCHOOL] = "small"
    REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(
        dateMap
    )
        .addOnSuccessListener {
            viewModel.setYearsOldChild("Выберите день рождения")
            navController.navigate(mainActivity.resources.getString(R.string.user_data_nav))
        }
        .addOnFailureListener {
            navController.navigate(mainActivity.resources.getString(R.string.enter_phone_nav))
        }
}
private fun usersDataBig(
    uid: String,
    viewModel: MainViewModel,
    messagingToken: String,
    navController: NavHostController,
    mainActivity: MainActivity
) {
    val dateMap = mutableMapOf<String, Any>()
    dateMap[CHILD_ID] = uid
    dateMap[CHILD_PHONE] = viewModel.getPhoneNumber()
    dateMap[CHILD_REGISTRATION_TIMESTAMP] = ServerValue.TIMESTAMP
    dateMap[CHILD_TOKEN] = messagingToken
    dateMap[CHILD_SCHOOL] = "big"
    REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(
        dateMap
    )
        .addOnSuccessListener {

            navController.navigate("Взрослый")
        }
        .addOnFailureListener {
            navController.navigate(mainActivity.resources.getString(R.string.enter_phone_nav))
        }
}


private fun enterCode(
    string: String,
    viewModel: MainViewModel,
    navController: NavHostController,
    mainActivity: MainActivity
) {
    val credential = PhoneAuthProvider.getCredential(viewModel.getId(), string)
    AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            createNodes(viewModel, navController, mainActivity)
        } else {
            viewModel.setCode("")
            showToast(mainActivity.resources.getString(R.string.err_message1), mainActivity)
        }
    }
}

