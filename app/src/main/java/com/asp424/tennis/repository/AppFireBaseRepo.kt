package com.asp424.tennis.repository

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.asp424.tennis.*
import com.asp424.tennis.models.EventModel
import com.asp424.tennis.models.ModelMessage
import com.asp424.tennis.models.TrainerModel
import com.asp424.tennis.models.UserModel
import com.asp424.tennis.room.*
import com.asp424.tennis.utils.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AppFireBaseRepo {
    fun getClientsList(
        mDb: UserDao,
        function: (MutableList<UserModel>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val list = mutableListOf<UserModel>()
        var counter = 0
        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(CHILD_CLIENTS)
            .addListenerForSingleValueEvent(AppValueEventListener { fromTrainer ->
                CoroutineScope(Dispatchers.IO).launch {
                    if (fromTrainer.childrenCount.toInt() != mDb.getAllUsers().size) {
                        mDb.deleteAllUsers()
                        fromTrainer.children.map {
                            counter++
                            if (!list.contains(it.getUserModel()))
                                list.add(it.getUserModel())
                            if (counter == fromTrainer.childrenCount.toInt())
                                function(list)
                        }
                    } else {
                        fromTrainer.children.map {
                            counter++
                            if (!list.contains(it.getUserModel()))
                                list.add(it.getUserModel())
                            if (counter == fromTrainer.childrenCount.toInt())
                                function(list)
                        }
                    }
                }
            })
    }

    fun setToRoom(viewModel: MainViewModel, mDb: UserDao, mutableList: MutableList<UserModel>) =
        CoroutineScope(Dispatchers.IO).launch {
            var counter = 0
            mutableList.forEach { list ->
                counter++
                if (list.key.isNotEmpty()
                ) {
                    if (!mDb.existsUsersList(list.key)) {
                        mDb.insertAllUsers(
                            User(
                                list.key,
                                list.patronymic,
                                list.name,
                                list.secondName,
                                list.phone_of_parent,
                                list.city_of_training,
                                list.name_child,
                                list.secondName_child,
                                list.years_old_child,
                                list.token,
                                list.color.toString(),
                                list.name_group,
                                list.time,
                                list.years,
                                list.e_mail,
                                list.school
                            )
                        )
                    } else mDb.updateAllUsers(
                        User(
                            list.key,
                            list.patronymic,
                            list.name,
                            list.secondName,
                            list.phone_of_parent,
                            list.city_of_training,
                            list.name_child,
                            list.secondName_child,
                            list.years_old_child,
                            list.token,
                            list.color.toString(),
                            list.name_group,
                            list.time,
                            list.years,
                            list.e_mail,
                            list.school
                        )
                    )
                }
                if (counter == mutableList.size)
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.setListClients(mDb.getAllUsers())
                    }
            }
        }

    fun getClientsListBig(
        mDb: UserDao,
        function: (MutableList<UserModel>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val list = mutableListOf<UserModel>()
        val listReady = mutableListOf<UserModel>()
        var counter = 0
        var counter1 = 0
        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(CHILD_CLIENTS)
            .addListenerForSingleValueEvent(AppValueEventListener { fromTrainer ->

                fromTrainer.children.forEach {
                    counter++
                    if (it.getUserModel().school == "big")
                    {list.add(it.getUserModel())
                        Log.d("My", list.toString())
                    }
                    if (counter == fromTrainer.childrenCount.toInt())
                    {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (list.size != mDb.getAllUsersBig().size) {
                                mDb.deleteAllUsersBig()
                                list.forEach {user ->
                                    counter1++
                                    if (!listReady.contains(user))
                                        listReady.add(user)
                                    if (counter1 == list.size)
                                        function(listReady)
                                }
                            } else {
                                list.forEach {user ->
                                    counter1++
                                    if (!listReady.contains(user))
                                        listReady.add(user)
                                    if (counter1 == list.size)
                                        function(listReady)
                                }
                            }
                        }
                    }
                }
            })
    }

    fun setToRoomBig(viewModel: MainViewModel, mDb: UserDao, mutableList: MutableList<UserModel>) =
        CoroutineScope(Dispatchers.IO).launch {
            var counter = 0

            mutableList.forEach { list ->
                counter++
                if (list.key.isNotEmpty()
                ) {
                    if (!mDb.existsUsersBig(list.key)) {
                        mDb.insertAllUsersBig(
                            UserBig(
                                list.key,
                                list.patronymic,
                                list.name,
                                list.secondName,
                                list.phone_of_parent,
                                list.city_of_training,
                                list.token,
                                list.color.toString(),
                                list.name_group,
                                list.time,
                                list.e_mail,
                                list.school
                            )
                        )
                    } else mDb.updateAllUsersBig(
                        UserBig(
                            list.key,
                            list.patronymic,
                            list.name,
                            list.secondName,
                            list.phone_of_parent,
                            list.city_of_training,
                            list.token,
                            list.color.toString(),
                            list.name_group,
                            list.time,
                            list.e_mail,
                            list.school
                        )
                    )
                }
                if (counter == mutableList.size)
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.setListClientsBig(mDb.getAllUsersBig())
                    }
            }
        }
    fun getListClientsFromBD(
        viewModel: MainViewModel
    ) {
        val list = mutableListOf<UserModel>()
        val mRefMainList = REF_DATABASE_ROOT.child(NODE_USERS)
        val mLMainListListener = AppValueEventListener { mainList ->
            mainList.children.map {
                if (it.getUserModel().school == "small")
                {
                    list.add(it.getUserModel())
                    viewModel.setListClientsBD(list)
                }
            }
        }
        mRefMainList.addListenerForSingleValueEvent(mLMainListListener)
    }

    fun getListClientsFromBDBig(
        viewModel: MainViewModel
    ) {
        val list = mutableListOf<UserModel>()
        val mRefMainList = REF_DATABASE_ROOT.child(NODE_USERS)
        val mLMainListListener = AppValueEventListener { mainList ->
            mainList.children.map {
                if (it.getUserModel().school == "big")
                {
                    list.add(it.getUserModel())
                    viewModel.setListClientsBDBig(list)
                }
            }
        }
        mRefMainList.addListenerForSingleValueEvent(mLMainListListener)
    }

    fun createGroup(
        mainViewModel: MainViewModel,
        mDb: UserDao,
        navControllerTrainer: NavHostController,
        mainActivity: MainActivity
    ) {
        val dateMap = mutableMapOf<String, Any>()
        if (mainViewModel.getCheckListMap().isNotEmpty()) {
            if (mainViewModel.getGroupName().isNotEmpty()) {
                mainViewModel.getCheckListMap().forEach { list ->
                    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
                        CHILD_CLIENTS
                    ).child(list.value).addListenerForSingleValueEvent(
                        AppValueEventListener { _ ->
                            dateMap[CHILD_COLOR] = mainViewModel.getGroupColor()
                            dateMap[CHILD_NAME_GROUP] = mainViewModel.getGroupName()
                            REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID)
                                .child(CHILD_CLIENTS).child(list.value)
                                .updateChildren(dateMap).addOnCompleteListener {
                                    val map = hashMapOf<String, Any>()
                                    map[list.value] = mainViewModel.getGroupName()
                                    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID)
                                        .child("groups")
                                        .child(mainViewModel.getGroupColor().toString())
                                        .updateChildren(map)
                                        .addOnCompleteListener {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                mDb.getAllUsers().forEach {
                                                    mDb.deleteUser(it)
                                                }
                                            }
                                            navControllerTrainer.navigate("Группы") {
                                                popUpTo("Группы") { inclusive = true }
                                            }
                                        }
                                }
                        }
                    )
                }
            } else showToast("Введите название группы", mainActivity)
        } else showToast("Выберите участников", mainActivity)

    }

    fun getListColors(function: (List<Color>) -> Unit) {
        val list = mutableListOf(
            Color.Red,
            Blue300,
            Color.Green,
            Color.Yellow,
        )
        var counter = 0
        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(NODE_GROUPS)
            .addListenerForSingleValueEvent(AppValueEventListener { colors ->
                if (colors.childrenCount.toInt() != 0) {
                    colors.children.forEach { color ->
                        counter++
                        list.remove(
                            when (color.key.toString()) {
                                "1" -> Color.Red
                                "2" -> Blue300
                                "3" -> Color.Green
                                "4" -> Color.Yellow
                                else -> Color.White
                            }
                        )
                        if (counter == colors.childrenCount.toInt()) function(list)
                    }
                } else function(list)
            })
    }

    fun getGroupEdit(viewModel: MainViewModel, color: Int, function: () -> Unit) {
        val list = hashMapOf<String, Boolean>()
        var counter = 0
        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
            CHILD_CLIENTS
        ).addListenerForSingleValueEvent(AppValueEventListener {
            it.children.forEach { listUid ->
                if (listUid.getUserModel().color == color)
                    list[listUid.getUserModel().key] = true
                counter++
                if (it.childrenCount.toInt() == counter)
                    viewModel.checkState.value = list
                function()
            }

        })

    }

    fun changeGroup(
        viewModel: MainViewModel,
        navControllerTrainer: NavHostController,
        mainActivity: MainActivity
    ) = CoroutineScope(Dispatchers.IO).launch {
        var counter = 0
        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
            CHILD_CLIENTS
        ).addListenerForSingleValueEvent(AppValueEventListener {
            it.children.forEach { color ->
                counter++
                if (color.getUserModel().color == viewModel.getGroupColor()) {
                    val map = hashMapOf<String, Any>()
                    map["color"] = 0
                    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
                        CHILD_CLIENTS
                    ).child(color.key.toString()).updateChildren(map)
                }
                if (counter == it.childrenCount.toInt())
                    hui(viewModel, navControllerTrainer, mainActivity)
            }
        })


    }

    private fun hui(
        viewModel: MainViewModel,
        navControllerTrainer: NavHostController,
        mainActivity: MainActivity
    ) {
        var counter = 0
        if (viewModel.getGroupName().isNotEmpty()) {
            viewModel.checkState.value?.forEach { list ->
                counter++
                if (list.value) {
                    val hashMap = hashMapOf<String, Any>()
                    hashMap[CHILD_COLOR] = viewModel.getGroupColor()
                    hashMap[CHILD_NAME_GROUP] = viewModel.getGroupName()
                    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
                        CHILD_CLIENTS
                    ).child(list.key).updateChildren(hashMap).addOnCompleteListener {
                        if (counter == viewModel.checkState.value!!.size)
                            navControllerTrainer.navigate("Группы")
                    }
                }
            }
        } else showToast("Введите название группы", mainActivity)

    }

    fun deleteGroup(mainViewModel: MainViewModel) {
        var counter = 0
        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
            CHILD_CLIENTS
        ).addListenerForSingleValueEvent(AppValueEventListener { sex ->
            sex.children.forEach { colorHui ->
                counter++
                if (colorHui.getUserModel().color == mainViewModel.getGroupColor()) {
                    val map = hashMapOf<String, Any>()
                    map["color"] = 0
                    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
                        CHILD_CLIENTS
                    ).child(colorHui.getUserModel().key).updateChildren(map)
                        .addOnCompleteListener {
                            if (counter == sex.childrenCount.toInt())
                                mainViewModel.getListClientsTrainer()
                            deleteEventsGroup(mainViewModel)
                        }
                }
            }
        })
    }

    private fun deleteEventsGroup(mainViewModel: MainViewModel) =
        CoroutineScope(Dispatchers.IO).launch {
            REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
                "events"
            ).addListenerForSingleValueEvent(AppValueEventListener {
                it.children.forEach { event ->
                    if (event.getUserModel().color == mainViewModel.getGroupColor() && event.getUserModel().mark == "group")
                        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
                            "events"
                        ).child(event.key.toString()).removeValue()
                    if (event.getUserModel().color == mainViewModel.getGroupColor() && event.getUserModel().mark == "user") {
                        val map = hashMapOf<String, Any>()
                        map["color"] = 0
                        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child(
                            "events"
                        ).child(event.key.toString()).updateChildren(map)
                    }
                }
            })
        }

    fun createNewClient(
        mainViewModel: MainViewModel,
        navControllerTrainer: NavHostController,
        mainActivity: MainActivity
    ) {
        CURRENT_UID = AUTH.currentUser?.uid.toString()
        mainViewModel.apply {
            val key = REF_DATABASE_ROOT.push().key
            if (getUsernameChild().isNotEmpty() && getSecondNameUserChild().isNotEmpty() && getYearsOldChild() != "Выберите день рождения" && getYearsOldChild() != "") {
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_NAME] = getUsernamePar()
                dateMap[CHILD_SECONDNAME] = getSecondNameUserPar()
                dateMap[CHILD_PATRONYMIC] = getPatronymicPar()
                dateMap[CHILD_NAME_CHILD] = getUsernameChild()
                dateMap[CHILD_SECONDNAME_CHILD] = getSecondNameUserChild()
                dateMap[CHILD_YEARS_CHILD] = getYearsOldChild()
                dateMap[CHILD_CITY] = getTrainUserCity()
                dateMap[CHILD_USER_PLAN] = getTrainUserPlan()
                dateMap[CHILD_CLIENTS_PHONE] = getNewClientPhone()
                dateMap[KEY] = key.toString()
                dateMap["years"] = mainViewModel.yearsChild.value.toString()
                key?.let {
                    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID)
                        .child(CHILD_CLIENTS)
                        .child(it)
                        .updateChildren(
                            dateMap
                        ).addOnCompleteListener {
                            navControllerTrainer.navigate("Спортсмены")
                        }
                }
            } else showToast(
                "Введите, хотя бы, имя и фамилию ученика",
                mainActivity
            )
        }
    }

    fun addClientFromBD(mainViewModel: MainViewModel, navControllerTrainer: NavHostController) {
        val dateMap = mutableMapOf<String, Any>()
        var counter = 0
        mainViewModel.getCheckListMap().forEach { it1 ->
            counter++
            REF_DATABASE_ROOT.child(NODE_USERS).child(it1.value)
                .addListenerForSingleValueEvent(
                    AppValueEventListener { user ->
                        val client = user.getUserModel()
                        dateMap[CHILD_NAME] = client.name
                        dateMap[CHILD_SECONDNAME] = client.secondName
                        dateMap[CHILD_PATRONYMIC] = client.patronymic
                        dateMap[CHILD_NAME_CHILD] = client.name_child
                        dateMap[CHILD_SECONDNAME_CHILD] = client.secondName_child
                        dateMap[CHILD_YEARS_CHILD] = client.years_old_child
                        dateMap[CHILD_CITY] = client.city_of_training
                        dateMap[CHILD_CLIENTS_PHONE] = client.phone
                        dateMap[KEY] = client.key
                        dateMap[CHILD_TOKEN] = client.token
                        dateMap[CHILD_SCHOOL] = client.school
                        dateMap["years"] = client.years
                        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID)
                            .child(CHILD_CLIENTS).child(it1.key)
                            .updateChildren(dateMap).addOnCompleteListener {
                                val map = hashMapOf<String, Any>()
                                map["id"] = CURRENT_UID
                                REF_DATABASE_ROOT.child(NODE_USERS).child(it1.value).child(
                                    NODE_TRAINERS
                                ).child(REF_DATABASE_ROOT.push().key.toString()).updateChildren(map)
                                if (counter == mainViewModel.getCheckListMap().size)
                                    if (client.school == "small")
                                    navControllerTrainer.navigate("Спортсмены")
                                else navControllerTrainer.navigate("Клиенты")
                            }
                    })
        }
    }

    fun deleteClient(id: String, mainViewModel: MainViewModel, mDb: UserDao) {
        CoroutineScope(Dispatchers.IO).launch {
            id.let { it1 ->
                REF_DATABASE_ROOT
                    .child(NODE_TRAINERS)
                    .child(CURRENT_UID)
                    .child(CHILD_CLIENTS)
                    .child(it1)
                    .removeValue()
                    .addOnCompleteListener { _ ->
                        CoroutineScope(Dispatchers.Main).launch {
                            REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID)
                                .child("events")
                                .addListenerForSingleValueEvent(AppValueEventListener { it1 ->
                                    it1.children.forEach { client ->
                                        if (client.getUserModel().client == id && client.getUserModel().mark == "user")
                                            REF_DATABASE_ROOT.child(NODE_TRAINERS)
                                                .child(CURRENT_UID).child("events")
                                                .child(client.key.toString()).removeValue()
                                    }
                                })
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            mDb.delete(id)
                            if (mDb.getAllUsers().size == 0
                            ) {
                                mainViewModel.setListClients(mutableListOf())
                            }
                            mainViewModel.getListClientsTrainer()
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            REF_DATABASE_ROOT.child(NODE_USERS)
                                .addListenerForSingleValueEvent(
                                    AppValueEventListener {
                                        it.children.forEach { user ->
                                            user.child(NODE_TRAINERS).children.forEach { trainer ->
                                                if (trainer.getTrainerModel().id == CURRENT_UID)
                                                    REF_DATABASE_ROOT.child(
                                                        NODE_USERS
                                                    )
                                                        .child(user.getUserModel().id)
                                                        .child(
                                                            NODE_TRAINERS
                                                        )
                                                        .child(trainer.key.toString())
                                                        .removeValue()
                                            }
                                        }
                                    })
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            REF_DATABASE_ROOT.child(NODE_USERS)
                                .addListenerForSingleValueEvent(AppValueEventListener { users ->
                                    users.children.forEach { user ->
                                        user.child("events").children.forEach { event ->
                                            if (event.getEventModel().trainer == CURRENT_UID)
                                                REF_DATABASE_ROOT.child(NODE_USERS).child(
                                                    user.getUserModel()
                                                        .id
                                                ).child("events")
                                                    .child(event.key.toString()).removeValue()
                                        }
                                    }
                                })
                        }
                    }
            }
        }
    }


    fun deleteClientBig(id: String, mainViewModel: MainViewModel, mDb: UserDao) {
        CoroutineScope(Dispatchers.IO).launch {
            id.let { it1 ->
                REF_DATABASE_ROOT
                    .child(NODE_TRAINERS)
                    .child(CURRENT_UID)
                    .child(CHILD_CLIENTS)
                    .child(it1)
                    .removeValue()
                    .addOnCompleteListener { _ ->
                        CoroutineScope(Dispatchers.Main).launch {
                            REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID)
                                .child("events")
                                .addListenerForSingleValueEvent(AppValueEventListener { it1 ->
                                    it1.children.forEach { client ->
                                        if (client.getUserModel().client == id && client.getUserModel().mark == "user")
                                            REF_DATABASE_ROOT.child(NODE_TRAINERS)
                                                .child(CURRENT_UID).child("events")
                                                .child(client.key.toString()).removeValue()
                                    }
                                })
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            mDb.deleteBig(id)
                            if (mDb.getAllUsersBig().size == 0
                            ) {
                                mainViewModel.setListClientsBig(mutableListOf())
                            }
                            mainViewModel.getListClientsTrainerBig()
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            REF_DATABASE_ROOT.child(NODE_USERS)
                                .addListenerForSingleValueEvent(
                                    AppValueEventListener {
                                        it.children.forEach { user ->
                                            user.child(NODE_TRAINERS).children.forEach { trainer ->
                                                if (trainer.getTrainerModel().id == CURRENT_UID)
                                                    REF_DATABASE_ROOT.child(
                                                        NODE_USERS
                                                    )
                                                        .child(user.getUserModel().id)
                                                        .child(
                                                            NODE_TRAINERS
                                                        )
                                                        .child(trainer.key.toString())
                                                        .removeValue()
                                            }
                                        }
                                    })
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            REF_DATABASE_ROOT.child(NODE_USERS)
                                .addListenerForSingleValueEvent(AppValueEventListener { users ->
                                    users.children.forEach { user ->
                                        user.child("events").children.forEach { event ->
                                            if (event.getEventModel().trainer == CURRENT_UID)
                                                REF_DATABASE_ROOT.child(NODE_USERS).child(
                                                    user.getUserModel()
                                                        .id
                                                ).child("events")
                                                    .child(event.key.toString()).removeValue()
                                        }
                                    }
                                })
                        }
                    }
            }
        }
    }

    fun getCodeAdmin(function: (code: String) -> Unit) {
        REF_DATABASE_ROOT.child(CHILD_CODE).addListenerForSingleValueEvent(AppValueEventListener {
            val code = it.value
            function(code.toString())
        })
    }


    fun getTrainersList(
        function: (MutableList<TrainerModel>) -> Unit
    ) = CoroutineScope(Dispatchers.Main).launch {
        val list = mutableListOf<TrainerModel>()
        var counter = 0
        REF_DATABASE_ROOT.child(NODE_TRAINERS)
            .addListenerForSingleValueEvent(AppValueEventListener { fromTrainer ->
                fromTrainer.children.forEach {
                    counter++
                    if (!list.contains(it.getTrainerModel()))
                        list.add(it.getTrainerModel())
                    if (counter == fromTrainer.childrenCount.toInt())
                        function(list)
                }
            })
    }

    fun setTrainersToRoom(
        viewModel: MainViewModel,
        mDb: UserDao,
        mutableList: MutableList<TrainerModel>
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            if (mutableList.size != mDb.getAllTrainers().size)
                mDb.deleteAllTrainers()
            mutableList.forEach { list ->
                if (list.id.isNotEmpty()
                ) {
                    if (!mDb.existsTrainersList(list.id)) {
                        mDb.insertAllTrainers(
                            TrainerMod(
                                list.id,
                                list.e_mail,
                                list.gps_time.toString(),
                                list.id,
                                list.latitude,
                                list.longitude,
                                list.name,
                                list.patronymic,
                                list.phone,
                                list.secondName,
                                list.timeRegistrationStamp.toString(),
                                list.token,
                                list.whatsapp_number,
                                list.workPlace,
                            )
                        )


                    } else mDb.updateAllTrainers(
                        TrainerMod(
                            list.id,
                            list.e_mail,
                            list.gps_time.toString(),
                            list.id,
                            list.latitude,
                            list.longitude,
                            list.name,
                            list.patronymic,
                            list.phone,
                            list.secondName,
                            list.timeRegistrationStamp.toString(),
                            list.token,
                            list.whatsapp_number,
                            list.workPlace,
                        )
                    )
                }
            }
            delay(1000L)
            launch {
                viewModel.setListTrainers(mDb.getAllTrainers())
            }
        }

    fun getEventsFromBD(mDb: UserDao, mainViewModel: MainViewModel, function: () -> Unit) =
        CoroutineScope(Dispatchers.Main).launch {
            val list = mutableListOf<EventModel>()
            REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).child("events")
                .addListenerForSingleValueEvent(AppValueEventListener { it1 ->
                    it1.children.forEach {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (!mDb.existsEventList(it.key.toString())) {
                                mDb.insertAllEvents(
                                    EventModelRoom(
                                        uid = it.key.toString(),
                                        name = it.getEventModel().name,
                                        start = it.getEventModel().start,
                                        end = it.getEventModel().end,
                                        desc = it.getEventModel().desc,
                                        color = it.getEventModel().color.toString()
                                    )
                                )
                            }
                        }
                        val model = EventModel(
                            name = it.getEventModel().name,
                            start = LocalDateTime.parse(it.getEventModel().start),
                            end = LocalDateTime.parse(it.getEventModel().end),
                            color = when (it.getEventModel().color.toString()) {
                                "1" -> Color(0xFFF70606)
                                "2" -> Color(0xFF3967DB)
                                "3" -> Color(0xFF368A08)
                                "4" -> Color(0xFFFFEB3B)
                                else -> Color(0xFFE9E3E3)
                            }, desc = it.getEventModel().desc
                        )
                        list.add(model)

                    }
                    if (list.size == it1.childrenCount.toInt())
                        mainViewModel.eventList.value = list
                    function()

                })
        }

    fun getEventsFromBDUser(
        mainActivity: MainActivity,
        navControllerUser: NavHostController,
        mDb: UserDao,
        mainViewModel: MainViewModel,
        function: () -> Unit
    ) =
        CoroutineScope(Dispatchers.Main).launch {
            val list = mutableListOf<EventModel>()
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child("events")
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.value == null) {
                        showToast("Вы ещё не добавлены в расписание", mainActivity)
                        navControllerUser.navigate("Тренер")
                    } else {
                        it.children.forEach { event ->
                            CoroutineScope(Dispatchers.IO).launch {
                                if (!mDb.existsEventList(event.key.toString())) {
                                    mDb.insertAllEvents(
                                        EventModelRoom(
                                            uid = event.key.toString(),
                                            name = event.getEventModel().name,
                                            start = event.getEventModel().start,
                                            end = event.getEventModel().end,
                                            desc = "",
                                            color = event.getEventModel().color.toString()
                                        )
                                    )
                                }
                            }
                            val model = EventModel(
                                name = event.getEventModel().name,
                                start = LocalDateTime.parse(event.getEventModel().start),
                                end = LocalDateTime.parse(event.getEventModel().end),
                                color = Color.Gray,
                                desc = ""
                            )
                            list.add(model)
                            if (list.size == it.childrenCount.toInt())
                                mainViewModel.eventList.value = list
                            function()
                        }
                    }
                })
        }

    fun deleteFromRasp(event: LocalDateTime, mDb: UserDao, function: () -> Unit) {
        REF_DATABASE_ROOT
            .child(NODE_TRAINERS)
            .child(
                CURRENT_UID
            )
            .child("events")
            .addListenerForSingleValueEvent(
                AppValueEventListener {
                    it.children.forEach { child ->
                        if (child.getEventModel().start == "$event:00")
                            REF_DATABASE_ROOT
                                .child(NODE_TRAINERS)
                                .child(CURRENT_UID)
                                .child("events")
                                .child(child.key.toString())
                                .removeValue()
                                .addOnCompleteListener {
                                    CoroutineScope(
                                        Dispatchers.IO
                                    ).launch {
                                        mDb.deleteEvent(
                                            child.key.toString()
                                        )
                                    }
                                    function()
                                }
                    }
                })
    }

    fun getNameOfTrainerForNotif(id: String, function: (name: String) -> Unit) {
        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(id)
            .addListenerForSingleValueEvent(AppValueEventListener {
                function(it.getTrainerModel().name + " " + it.getTrainerModel().patronymic + " " + it.getTrainerModel().secondName)
            })
    }

    fun getNameOfUserForNotif(id: String, function: (name: String) -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(id)
            .addListenerForSingleValueEvent(AppValueEventListener {
                function(it.getUserModel().name_child + " " + it.getUserModel().secondName_child)
            })
    }

    fun sendMessage(
        who: String,
        message: String,
        receivingUserID: String,
        function: () -> Unit,
    ) = CoroutineScope(Dispatchers.IO).launch {
        val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserID"
        val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/$CURRENT_UID"
        val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key
        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_FROM] = CURRENT_UID
        mapMessage[CHILD_WHO] = who
        mapMessage[CHILD_TEXT] = message
        mapMessage[CHILD_ID] = messageKey.toString()
        mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
        val mapMessageRes = hashMapOf<String, Any>()
        mapMessageRes[CHILD_FROM] = CURRENT_UID
        mapMessageRes[CHILD_WHO] = who
        mapMessageRes[CHILD_TEXT] = message
        mapMessageRes[CHILD_ID] = messageKey.toString()
        mapMessageRes[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
        val mapDialog = hashMapOf<String, Any>()
        mapDialog["$refDialogUser/$messageKey"] = mapMessage
        mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessageRes
        REF_DATABASE_ROOT
            .updateChildren(mapDialog)
            .addOnSuccessListener { function() }
    }

    fun sendMessageFromTrainer(
        message: String,
        receivingUserID: String,
        function: () -> Unit,
    ) = CoroutineScope(Dispatchers.IO).launch {
        val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserID"
        val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/$CURRENT_UID"
        val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key
        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_FROM] = CURRENT_UID
        mapMessage[CHILD_WHO] = "trainer"
        mapMessage[CHILD_TEXT] = message
        mapMessage[CHILD_ID] = messageKey.toString()
        mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
        val mapMessageRes = hashMapOf<String, Any>()
        mapMessageRes[CHILD_FROM] = CURRENT_UID
        mapMessageRes[CHILD_WHO] = "trainer"
        mapMessageRes[CHILD_TEXT] = message
        mapMessageRes[CHILD_ID] = messageKey.toString()
        mapMessageRes[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

        val mapDialog = hashMapOf<String, Any>()
        mapDialog["$refDialogUser/$messageKey"] = mapMessage
        mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessageRes
        REF_DATABASE_ROOT
            .updateChildren(mapDialog)
            .addOnSuccessListener { function() }
    }

    fun checkForUser(key: String, function: (Boolean) -> Unit) {
        var check = false
        var counter = 0
        REF_DATABASE_ROOT.child(NODE_USERS).addListenerForSingleValueEvent(AppValueEventListener {
            it.children.forEach { have ->
                counter++
                if (key == have.getUserModel().key)
                    check = true
                if (counter == it.childrenCount.toInt())
                    function(check)
            }
        })
    }

    fun getMessages(id: String, viewModel: MainViewModel, function: () -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(id)
                .addValueEventListener(AppValueEventListener {
                    var counter = 0
                    val list = mutableListOf<ModelMessage>()
                    it.children.forEach { message ->
                        counter++
                        list.add(message.getMessageModel())
                        if (counter == it.childrenCount.toInt())
                            viewModel._messages.postValue(list)
                        function()
                    }
                })
        }

    fun getUserUid(key: String, function: (String) -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .addListenerForSingleValueEvent(AppValueEventListener {
                it.children.forEach { user ->
                    if (user.getUserModel().key == key)
                        function(user.key.toString())
                }
            })
    }

    fun getMessagesUser(
        id: String,
        viewModel: MainViewModel,
        trainerId: String,
        function: () -> Unit
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            REF_DATABASE_ROOT.child(NODE_MESSAGES).child(id).child(trainerId)
                .addValueEventListener(AppValueEventListener {
                    var counter = 0
                    val list = mutableListOf<ModelMessage>()
                    it.children.forEach { message ->
                        counter++
                        list.add(message.getMessageModel())
                        if (counter == it.childrenCount.toInt())
                            viewModel._messages.postValue(list)
                        function()
                    }
                })
        }

    fun getUserKey(function: (key: String) -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                function(it.getUserModel().key)
            })
    }

    fun clearChat(id: String, viewModel: MainViewModel) = CoroutineScope(Dispatchers.IO).launch {
        REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(id)
            .removeValue()
            .addOnSuccessListener {
                REF_DATABASE_ROOT.child(NODE_MESSAGES).child(id).child(CURRENT_UID).removeValue()
                    .addOnCompleteListener {
                        viewModel._messages.postValue(mutableListOf())
                    }
            }
    }

    fun getUsersTrainer(
        mDb: UserDao,
        mainViewModel: MainViewModel,
        function: (MutableList<String>) -> Unit
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            var counter = 0
            val list = mutableListOf<String>()
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_TRAINERS)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.value != null)
                        it.children.forEach { trainer ->
                            counter++
                            if (!list.contains(trainer.getTrainerModel().id))
                                trainer.getTrainerModel().id.let { it1 -> list.add(it1) }
                            if (counter == it.childrenCount.toInt())
                                function(list)
                        }
                    else CoroutineScope(Dispatchers.IO).launch {
                        if (mDb.getAllTrainers().isNotEmpty()) {
                            mDb.deleteAllTrainers()
                            mainViewModel.setListTrainers(mutableListOf())
                        }

                    }
                })
        }

    fun getModelsOfUsersTrainers(
        list: MutableList<String>,
        mainViewModel: MainViewModel,
        mDbDao: UserDao,
        function: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (list.size != mDbDao.getAllTrainers().size)
                mDbDao.deleteAllTrainers()
        }
        val listTrainers = mutableListOf<TrainerMod>()
        var counter = 0
        var counter1 = 0
        if (list.isNotEmpty())
            list.forEach { trainer ->
                counter++
                REF_DATABASE_ROOT.child(NODE_TRAINERS).child(trainer)
                    .addListenerForSingleValueEvent(AppValueEventListener { model ->
                        val her = model.getTrainerModel()
                        CoroutineScope(Dispatchers.IO).launch {
                            if (mDbDao.getAllTrainers().size == 0)
                                if (!mDbDao.existsTrainersList(her.id)) {
                                    mDbDao.insertAllTrainers(
                                        TrainerMod(
                                            her.id,
                                            her.e_mail,
                                            "",
                                            her.id,
                                            "",
                                            "",
                                            her.name,
                                            her.patronymic,
                                            her.phone,
                                            her.secondName,
                                            "",
                                            her.token,
                                            her.whatsapp_number,
                                            her.workPlace
                                        )
                                    )
                                }
                            listTrainers.add(
                                TrainerMod(
                                    her.id,
                                    her.e_mail,
                                    "",
                                    her.id,
                                    "",
                                    "",
                                    her.name,
                                    her.patronymic,
                                    her.phone,
                                    her.secondName,
                                    "",
                                    her.token,
                                    her.whatsapp_number,
                                    her.workPlace
                                )
                            )
                            if (list.size == counter)
                                CoroutineScope(Dispatchers.IO).launch {
                                    if (listTrainers.size == mDbDao.getAllTrainers().size)
                                        listTrainers.forEachIndexed { i, value ->
                                            if (mDbDao.getAllTrainers()[i] != value)
                                                mDbDao.deleteAllTrainers()
                                        }

                                    listTrainers.forEach { her ->
                                        counter1++
                                        if (!mDbDao.existsTrainersList(her.id!!)) {
                                            mDbDao.updateAllTrainers(
                                                TrainerMod(
                                                    her.id,
                                                    her.e_mail,
                                                    "",
                                                    her.id,
                                                    "",
                                                    "",
                                                    her.name,
                                                    her.patronymic,
                                                    her.phone,
                                                    her.secondName,
                                                    "",
                                                    her.token,
                                                    her.whatsapp_number,
                                                    her.workPlace
                                                )
                                            )
                                        }
                                    }
                                    if (counter1 == listTrainers.size) {
                                        mainViewModel.setListTrainers(mDbDao.getAllTrainers())
                                        function()
                                    }
                                } else function()
                        }
                    })
            }
    }

    fun getAdminsTrainerRasp(
        id: String,
        mDb: UserDao,
        mainViewModel: MainViewModel,
        function: () -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val list = mutableListOf<EventModel>()
        REF_DATABASE_ROOT.child(NODE_TRAINERS).child(id).child("events")
            .addListenerForSingleValueEvent(AppValueEventListener { it1 ->
                it1.children.forEach {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (!mDb.existsEventList(it.key.toString())) {
                            mDb.insertAllEvents(
                                EventModelRoom(
                                    uid = it.key.toString(),
                                    name = it.getEventModel().name,
                                    start = it.getEventModel().start,
                                    end = it.getEventModel().end,
                                    desc = it.getEventModel().desc,
                                    color = it.getEventModel().color.toString()
                                )
                            )
                        }
                    }
                    val model = EventModel(
                        name = it.getEventModel().name,
                        start = LocalDateTime.parse(it.getEventModel().start),
                        end = LocalDateTime.parse(it.getEventModel().end),
                        color = when (it.getEventModel().color.toString()) {
                            "1" -> Color(0xFFF70606)
                            "2" -> Color(0xFF3967DB)
                            "3" -> Color(0xFF368A08)
                            "4" -> Color(0xFFFFEB3B)
                            else -> Color(0xFFE9E3E3)
                        }, desc = it.getEventModel().desc
                    )
                    list.add(model)
                }
                if (list.size == it1.childrenCount.toInt())
                    mainViewModel.eventList.value = list
                function()
            })
    }

    fun getUserData(viewModel: MainViewModel, function: () -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                viewModel.apply {
                    val user = it.getUserModel()
                    setUsernamePar(user.name)
                    setSecondNameUserPar(user.secondName)
                    setPatronymicUserPar(user.patronymic)
                    setUsernameChild(user.name_child)
                    setSecondNameUserChild(user.secondName_child)
                    setYearsOldChild(user.years_old_child)
                    setTrainUserCity(user.city_of_training)
                }
                function()
            })
    }
}










