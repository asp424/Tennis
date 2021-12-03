package com.asp424.tennis.screens.registration

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.asp424.tennis.*
import com.asp424.tennis.R
import com.asp424.tennis.room.AppDatabase
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.utils.deleteSymbols
import com.asp424.tennis.utils.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RegScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val mainActivity = LocalContext.current as MainActivity
    var progress by remember { mutableStateOf(0.0f) }
    var progressNumber by remember { mutableStateOf(0.0f) }
    val animatedProgressRound = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value
    val animatedProgress = animateFloatAsState(
        targetValue = progressNumber,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value
    if (viewModel.getPhoneNumber() != stringResource(id = R.string.null_string))
        progressNumber = when (viewModel.getPhoneNumber().length) {
            0 -> 0.0f
            1 -> 0.083f
            2 -> 0.17f
            3 -> 0.249f
            4 -> 0.332f
            5 -> 0.415f
            6 -> 0.498f
            7 -> 0.581f
            8 -> 0.664f
            9 -> 0.747f
            10 -> 0.83f
            11 -> 0.913f
            12 -> 1f
            else -> 1f
        }

    val phone: String? by viewModel.phoneNumber.observeAsState("")
    Column(
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
    ) {
        TopBar(tittle = stringResource(id = R.string.enter_phone))
        Box(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Blue,
                    unfocusedBorderColor = Blue300,
                    cursorColor = Blue,
                    textColor = Blue,
                    unfocusedLabelColor = Blue300,
                    focusedLabelColor = Blue
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                value = phone!!,
                onValueChange = { new ->
                    viewModel.setValuePhone(new)
                }, label = {
                    Text(
                        text = stringResource(id = R.string.number),
                        style = TextStyle(fontStyle = FontStyle.Italic),
                        fontSize = 16.sp
                    )
                },
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 20.sp, fontStyle = FontStyle.Italic)
            )
        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            LinearProgressIndicator(progress = animatedProgress)
        }
        Text(
            modifier = Modifier.padding(16.dp), text =
            stringResource(id = R.string.reg_message),
            style = TextStyle(textAlign = TextAlign.Center)
        )
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(
                progress = animatedProgressRound,
                Modifier
                    .width(50.dp)
                    .padding(top = 70.dp)
            )
        }
    }
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp, end = 60.dp)
    ) {
        OutlinedButton(
            onClick = {
                if (progress < 1f) progress += 0.7f
                mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        AUTH.signInWithCredential(credential).addOnCompleteListener {
                            viewModel.setCode(credential.smsCode.toString())
                        }
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        showToast(
                            p0.toString(),
                            mainActivity
                        )
                        if (progress > 0f) progress = 0f
                    }
                    override fun onCodeSent(
                        id: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            if (progress > 0f) progress = 1f
                            viewModel.setId(id)
                            delay(900L)
                            navController.navigate(mainActivity.resources.getString(R.string.enter_code_nav))
                        }
                    }
                }
                if (deleteSymbols(viewModel.getPhoneNumber())!!.isNotEmpty()) {
                    val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(deleteSymbols(viewModel.getPhoneNumber())!!)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(mainActivity)
                        .setCallbacks(mCallback)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    if (progress > 0f) progress = 0f
                    showToast(mainActivity.resources.getString(R.string.err_message3), mainActivity)
                }
            },
            modifier = Modifier.size(60.dp),
            shape = CircleShape,
            border = BorderStroke(1.dp, Gray),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue),
            elevation = elevation(
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


