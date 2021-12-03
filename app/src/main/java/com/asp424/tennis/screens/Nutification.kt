package com.asp424.tennis.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tennis.*
import java.util.*

@Composable
fun NotifyScreen(viewModel: MainViewModel) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {

        TopBar(tittle = viewModel.getNotifyName())
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            var stateNot by remember {
                mutableStateOf("")
            }
            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = 10.dp, top = 15.dp),
                value = stateNot,
                onValueChange = { new ->
                    stateNot = new
                })
            Button(modifier = Modifier.padding(top = 3.dp), onClick = {
                val currentTime: Date = Calendar.getInstance().time
                val dateMap = mutableMapOf<String, Any>()
                dateMap["message"] = stateNot
                REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
                    CHILD_CLIENTS
                ).child(viewModel.getNotifyId())
                    .child(currentTime.toString())
                    .updateChildren(dateMap).addOnCompleteListener{stateNot = ""}
            }, content = {
                Text(
                    text = "Отправить напоминание",
                    style = TextStyle(fontSize = 10.sp)
                )
            })
        }
    }

}


/*
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
exports.notification = functions.database.ref('/trainers/{userUid}/clients/{key}/{ass}/message')
.onCreate((snapshot, context) => {
    var message = snapshot.val();
    const key = context.params.key;
    const userUid = context.params.userUid;
    return Promise.all([
        admin.database().ref(`/trainers/${userUid}/clients/${key}/token`).once('value')
    ]).then(values => {
        const token = values[0].val();
        const payload = {
            notification: {
            title: "Tennis",
            body: `${message}`,
            sound: 'default' ,
            click_action: 'CLICK_ACTION'
        }
            /*data: {userUid}*/
        };
        admin.messaging().sendToDevice(token, payload);  }
    )
});  */




