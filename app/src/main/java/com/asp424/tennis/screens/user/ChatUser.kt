package com.asp424.tennis.screens.user


import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tennis.*
import com.asp424.tennis.R
import com.asp424.tennis.models.ModelMessage
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.utils.appData
import com.asp424.tennis.utils.asDate
import com.asp424.tennis.utils.asTime
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ChatUser(viewModel: MainViewModel) {
    val listMessages: MutableList<ModelMessage>? by viewModel._messages.observeAsState(mutableListOf())
    val message: String? by viewModel.message.observeAsState("")
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    coroutineScope.launch {
        listState.animateScrollToItem(index = listMessages!!.size)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = viewModel.getNotifyName())
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .padding(bottom = 110.dp, top = 1.dp)
                .fillMaxSize()
                .background(backChat),
            verticalArrangement = Arrangement.Bottom, state = listState
        ) {
            listMessages!!.forEach {
                coroutineScope.launch { listState.animateScrollToItem(index = listMessages!!.size) }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                        horizontalArrangement = if (it.from == CURRENT_UID) Arrangement.End else Arrangement.Start
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = if (it.from == CURRENT_UID)
                                Alignment.End else Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSystemInDarkTheme()) Blue300 else Blue200)
                            ) {
                                Text(
                                    text = it.text,
                                    style = TextStyle(fontSize = 18.sp, color = Color.White),
                                    modifier = Modifier.padding(10.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Text(
                                text = it.timeStamp.toString()
                                    .asDate() + " в " + it.timeStamp.toString().asTime(),
                                style = TextStyle(fontSize = 12.sp, color = Color.White),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = if (it.from == CURRENT_UID)
                                    TextAlign.End else TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 55.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        TextField(value = message!!,
            onValueChange = { new -> viewModel.message.value = new },
            Modifier.padding(end = 14.dp, start = 10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            label = { Text(text = "Сообщение") },
            maxLines = 1,
            singleLine = true
        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(55.dp)) {
            Image(imageVector = Icons.Default.Send,
                contentDescription = null, modifier = Modifier.clickable {
                    if (message!!.isNotEmpty())
                        viewModel.sendMessage("user")
                    viewModel.message.value = ""
                    coroutineScope.launch { listState.animateScrollToItem(index = listMessages!!.size) }
                    keyboardController?.hide()
                })
        }
    }
    DropdownChat(viewModel = viewModel)
}

@ExperimentalAnimationApi
@Composable
fun DropdownChat(viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
            .padding(top = 3.dp)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.Default.MoreVert,
                tint = Color.White,
                contentDescription = stringResource(id = R.string.content_desc)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = { /* Handle refresh! */ }) {
                Text("item")
            }
            DropdownMenuItem(onClick = { /* Handle settings! */ }) {
                Text("item")
            }
            /*Divider()*/
            DropdownMenuItem(onClick = {
                expanded = false
                viewModel.clearChat()
            }) {
                Text("Очистить чат")
            }
        }
    }

}