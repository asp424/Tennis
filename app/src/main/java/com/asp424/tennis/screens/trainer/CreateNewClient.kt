package com.asp424.tennis.screens.trainer


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.asp424.tennis.MainActivity
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.R
import com.asp424.tennis.screens.CellOutField
import com.asp424.tennis.screens.CellTittle
import com.asp424.tennis.screens.RoundButton
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.screens.user.MenuCityEnter

@Composable
fun CreateNewClient(
    mainActivity: MainActivity,
    viewModel: MainViewModel,
    navControllerTrainer: NavHostController
) {
    val usernamePar: String? by viewModel.usernamePar.observeAsState("")
    val usernameChild: String? by viewModel.usernameChild.observeAsState("")
    val secondNameUserPar: String? by viewModel.secondNameUserPar.observeAsState("")
    val secondNameUserChild: String? by viewModel.secondNameUserChild.observeAsState("")
    val patronymicParUser: String? by viewModel.patronymicPar.observeAsState("")
    val phone: String? by viewModel.newClientPhone.observeAsState("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = "Новый спортсмен")
        Box(Modifier.padding(top = 8.dp)) {
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CellTittle(text = "Данные представителя спортсмена")
                CellOutField(
                    value = usernamePar,
                    text = stringResource(id = R.string.name),
                    KeyboardType.Text
                ) { new ->
                    viewModel.setUsernamePar(new)
                }
                CellOutField(
                    value = secondNameUserPar,
                    text = stringResource(id = R.string.second_name),
                    KeyboardType.Text
                ) { new ->
                    viewModel.setSecondNameUserPar(new)
                }
                CellOutField(
                    value = patronymicParUser,
                    text = "Отчество",
                    KeyboardType.Text
                ) { new ->
                    viewModel.setPatronymicUserPar(new)
                }
                CellOutField(value = phone, text = "Телефон", KeyboardType.Phone) { new ->
                    viewModel.setNewClientPhone(new)
                }
                CellTittle(text = "Город, в котором будет заниматься")
                MenuCityEnter(viewModel = viewModel)
                CellTittle(text = "Данные спортсмена")
                CellOutField(value = usernameChild, text = "Имя", KeyboardType.Text) { new ->
                    viewModel.setUsernameChild(new)
                }
                CellOutField(
                    value = secondNameUserChild,
                    text = stringResource(id = R.string.second_name),
                    KeyboardType.Text
                ) { new ->
                    viewModel.setSecondNameUserChild(new)
                }
                Card(modifier = Modifier
                    .width(280.dp)
                    .height(69.dp)
                    .padding(top = 10.dp)
                    .clickable {
                        navControllerTrainer.navigate("День рождения")
                    }, border = BorderStroke(1.dp, Gray)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()) {
                        Text(
                            text =  viewModel.getYearsOldChild(),
                            style = TextStyle(
                                fontSize = 17.sp,
                                textAlign = TextAlign.Center,
                                fontStyle = FontStyle.Italic,
                                color = Color.Blue
                            )
                        )
                    }
                }
                CellTittle(text = stringResource(id = R.string.plan))
                MenuTariff(viewModel = viewModel)
                Box(modifier = Modifier.padding(start = 230.dp, top = 40.dp, bottom = 80.dp)) {
                    RoundButton(icon = Icons.Default.ArrowForward) {
                        viewModel.createNewClientVM(
                            navControllerTrainer = navControllerTrainer,
                            mainActivity = mainActivity
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuTariff(viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val items =
        listOf(
            stringResource(id = R.string.plan1),
            stringResource(id = R.string.plan2),
            stringResource(id = R.string.plan3)
        )
    var selectedIndex by remember { mutableStateOf(0) }
    Card(
        modifier = Modifier
            .clickable(onClick = { expanded = true })
            .width(280.dp)
            .height(63.dp)
            .padding(top = 8.dp),
        border = BorderStroke(width = 0.5.dp, Gray)
    ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val plan = when (items[selectedIndex]) {
                stringResource(id = R.string.plan1) -> {
                    1
                }
                stringResource(id = R.string.plan2) -> {
                    2
                }
                stringResource(id = R.string.plan3) -> {
                    3
                }
                else -> 1
            }
            viewModel.setTrainUserPlan(plan.toString())
            Text(
                items[selectedIndex],
                Modifier.padding(start = 14.dp),
                style = TextStyle(fontSize = 16.sp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.background(Transparent)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                }) {
                    Card(
                        border = BorderStroke(width = 0.5.dp, color = Gray),
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(
                            text = s,
                            modifier = Modifier.padding(8.dp),
                            style = TextStyle(fontSize = 14.sp)
                        )
                    }
                }
            }
        }
    }
}

