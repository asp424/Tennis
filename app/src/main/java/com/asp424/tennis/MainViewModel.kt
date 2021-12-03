package com.asp424.tennis


import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.asp424.tennis.models.EventModel
import com.asp424.tennis.models.ModelMessage
import com.asp424.tennis.models.UserModel
import com.asp424.tennis.repository.AppFireBaseRepo
import com.asp424.tennis.room.TrainerMod
import com.asp424.tennis.room.User
import com.asp424.tennis.room.UserBig
import com.asp424.tennis.room.UserDao
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class MainViewModel(dao: UserDao) : ViewModel() {
    private val repoFB = AppFireBaseRepo()
    private val mDbDao = dao


    //PhoneNumber
    private var _phoneNumber = MutableLiveData("")
    var phoneNumber: LiveData<String> = _phoneNumber
    fun setValuePhone(phone: String) {
        _phoneNumber.value = phone
    }

    fun getPhoneNumber(): String {
        return phoneNumber.value.toString()
    }

    //RegId
    private var _id = MutableLiveData("")
    private var id: LiveData<String> = _id

    fun setId(id: String) {
        _id.value = id
    }

    fun getId(): String {
        return id.value.toString()
    }

    //Reg_code
    private var _code = MutableLiveData("")
    var code: LiveData<String> = _code

    fun setCode(codeReg: String) {
        _code.value = codeReg
    }

    fun getCode(): String {
        return code.value.toString()
    }

    //Trainer Data
    //Name
    private var _name = MutableLiveData("")
    var name: LiveData<String> = _name

    fun setName(nameTrainer: String) {
        _name.value = nameTrainer
    }

    fun getName(): String {
        return name.value.toString()
    }

    //SecondName
    private var _secondName = MutableLiveData("")
    var secondName: LiveData<String> = _secondName

    fun setSecondName(secondNameTrainer: String) {
        _secondName.value = secondNameTrainer
    }

    fun getSecondName(): String {
        return secondName.value.toString()
    }

    //Patronymic
    private var _patronymic = MutableLiveData("")
    var patronymic: LiveData<String> = _patronymic

    fun setPatronymic(patronymicTrainer: String) {
        _patronymic.value = patronymicTrainer
    }

    fun getPatronymic(): String {
        return patronymic.value.toString()
    }

    // WorkPlace
    private var _workPlace = MutableLiveData("")
    var workPlace: LiveData<String> = _workPlace

    fun setWorkPlace(workPlaceTrainer: String) {
        _workPlace.value = workPlaceTrainer
    }

    fun getWorkPlace(): String {
        return workPlace.value.toString()
    }

    // eMail
    private var _eMail = MutableLiveData("")
    var eMail: LiveData<String> = _eMail

    fun setEMail(eMailTrainer: String) {
        _eMail.value = eMailTrainer
    }

    fun getEMail(): String {
        return eMail.value.toString()
    }

    // WhatsApp
    private var _whatsapp = MutableLiveData("")
    var whatsapp: LiveData<String> = _whatsapp

    fun setWhatsapp(whatsappTrainer: String) {
        _whatsapp.value = whatsappTrainer
    }

    fun getWhatsapp(): String {
        return whatsapp.value.toString()
    }

    //ListOfClientsBD
    private var _listClientsBD = MutableLiveData<MutableList<UserModel>>(mutableListOf())
    var listClientsBD: LiveData<MutableList<UserModel>> = _listClientsBD

    fun setListClientsBD(listClientsStringTrainerBDMap: MutableList<UserModel>) {
        _listClientsBD.value = listClientsStringTrainerBDMap
    }

    fun getListClientsTrainerBD() {
        repoFB.getListClientsFromBD(this)
    }

    private var _listClientsBDBig = MutableLiveData<MutableList<UserModel>>(mutableListOf())
    var listClientsBDBig: LiveData<MutableList<UserModel>> = _listClientsBDBig

    fun setListClientsBDBig(listClientsStringTrainerBDMap: MutableList<UserModel>) {
        _listClientsBDBig.value = listClientsStringTrainerBDMap
    }

    fun getListClientsTrainerBDBig() {
        repoFB.getListClientsFromBDBig(this)
    }

    //ListOfClients
    private var _listClients = MutableLiveData<MutableList<User>>(mutableListOf())
    var listClients: LiveData<MutableList<User>> = _listClients

    fun setListClients(listClientsStringTrainer: MutableList<User>) {
        _listClients.postValue(listClientsStringTrainer)
    }

    fun getListClientsTrainer() {
        repoFB.getClientsList(mDbDao) {
            repoFB.setToRoom(this@MainViewModel, mDb = mDbDao, it)
        }
    }

    fun getListClientsTrainerBig() {
        repoFB.getClientsListBig(mDbDao) {
            repoFB.setToRoomBig(this@MainViewModel, mDb = mDbDao, it)
        }
    }

    private var _listClientsBig = MutableLiveData<MutableList<UserBig>>(mutableListOf())
    var listClientsBig: LiveData<MutableList<UserBig>> = _listClientsBig

    fun setListClientsBig(listClientsStringTrainer: MutableList<UserBig>) {
        _listClientsBig.postValue(listClientsStringTrainer)
    }

    private var _newClientPhone = MutableLiveData("")
    var newClientPhone: LiveData<String> = _newClientPhone

    fun setNewClientPhone(newClientPhoneString: String) {
        _newClientPhone.value = newClientPhoneString
    }

    fun getNewClientPhone(): String {
        return newClientPhone.value.toString()
    }

    //UserData
    //NamePar
    private var _usernamePar = MutableLiveData("")
    var usernamePar: LiveData<String> = _usernamePar

    fun setUsernamePar(usernameUserPar: String) {
        _usernamePar.value = usernameUserPar
    }

    fun getUsernamePar(): String {
        return usernamePar.value.toString()
    }

    //SecondNamePar
    private var _secondNameUserPar = MutableLiveData("")
    var secondNameUserPar: LiveData<String> = _secondNameUserPar

    fun setSecondNameUserPar(secondNameUserStringPar: String) {
        _secondNameUserPar.value = secondNameUserStringPar
    }

    fun getSecondNameUserPar(): String {
        return secondNameUserPar.value.toString()
    }

    //PatronymicPar
    private var _patronymicPar = MutableLiveData("")
    var patronymicPar: LiveData<String> = _patronymicPar

    fun setPatronymicUserPar(patronymicStringPar: String) {
        _patronymicPar.value = patronymicStringPar
    }

    fun getPatronymicPar(): String {
        return patronymicPar.value.toString()
    }

    //NameChild
    private var _usernameChild = MutableLiveData("")
    var usernameChild: LiveData<String> = _usernameChild

    fun setUsernameChild(usernameUserChild: String) {
        _usernameChild.value = usernameUserChild
    }

    fun getUsernameChild(): String {
        return usernameChild.value.toString()
    }

    //SecondNameChild
    private var _secondNameUserChild = MutableLiveData("")
    var secondNameUserChild: LiveData<String> = _secondNameUserChild

    fun setSecondNameUserChild(secondNameUserStringChild: String) {
        _secondNameUserChild.value = secondNameUserStringChild
    }

    fun getSecondNameUserChild(): String {
        return secondNameUserChild.value.toString()
    }

    //YearsOldChild
    private var _yearsOldChild = MutableLiveData("")
    var yearsOldChild: LiveData<String> = _yearsOldChild

    fun setYearsOldChild(yearsOldStringChild: String) {
        _yearsOldChild.value = yearsOldStringChild
    }

    fun getYearsOldChild(): String {
        return yearsOldChild.value.toString()
    }

    //TrainCity
    private var _trainUserCity = MutableLiveData("0")
    private var trainUserCity: LiveData<String> = _trainUserCity

    fun setTrainUserCity(trainUserCityString: String) {
        _trainUserCity.value = trainUserCityString
    }

    fun getTrainUserCity(): String {
        return trainUserCity.value.toString()
    }

    //Plan
    private var _planUserTrain = MutableLiveData("")
    private var planUserTrain: LiveData<String> = _planUserTrain

    fun setTrainUserPlan(trainUserPlanString: String) {
        _planUserTrain.value = trainUserPlanString
    }

    fun getTrainUserPlan(): String {
        return planUserTrain.value.toString()
    }

    //checkBoxMap
    private var _checkStateMap = MutableLiveData<HashMap<String, String>>(hashMapOf())
    private var checkStateMap: LiveData<HashMap<String, String>> = _checkStateMap
    fun initialCheckListMap() {
        _checkStateMap.value = hashMapOf()
    }

    fun setCheckListMap(id: String, name: String) {
        _checkStateMap.value!![id] = name
    }

    fun getCheckListMap(): HashMap<String, String> {
        return checkStateMap.value!!
    }

    //checkBox State
    var checkState = MutableLiveData<HashMap<String, Boolean>>(hashMapOf())


    //notifyMap
    private var _notifyId = MutableLiveData("")
    private var notifyId: LiveData<String> = _notifyId
    fun setNotifyId(id: String) {
        _notifyId.value = id
    }

    fun getNotifyId(): String {
        return notifyId.value!!
    }

    private var _notifyName = MutableLiveData("")
    private var notifyName: LiveData<String> = _notifyName

    fun setNotifyName(name: String) {
        _notifyName.value = name
    }

    fun getNotifyName(): String {
        return notifyName.value!!
    }

    private var _groupName = MutableLiveData("")
    var groupName: LiveData<String> = _groupName

    fun setGroupName(groupNameString: String) {
        _groupName.value = groupNameString
    }

    fun getGroupName(): String {
        return groupName.value.toString()
    }

    private var _groupColor = MutableLiveData(0)
    private var groupColor: LiveData<Int> = _groupColor

    fun setGroupColor(groupColorImt: Int) {
        _groupColor.value = groupColorImt
    }

    fun getGroupColor(): Int {
        return groupColor.value!!.toInt()
    }

    fun createClient() {
        setPatronymicUserPar("")
        setUsernameChild("")
        setUsernamePar("")
        setSecondNameUserPar("")
        setSecondNameUserChild("")
        setYearsOldChild("")
        setNewClientPhone("")
    }

    fun clearData() {
        setCode("")
        setName("")
        setSecondName("")
        setPatronymic("")
        setWorkPlace("")
        setEMail("")
        setWhatsapp("")
        setCode("")
        setUsernamePar("")
        setSecondNameUserPar("")
        setPatronymicUserPar("")
        setUsernameChild("")
        setSecondNameUserChild("")
        setYearsOldChild("")
        setTrainUserCity("")
    }

    fun createGroupVM(navControllerTrainer: NavHostController, mainActivity: MainActivity) {
        repoFB.createGroup(this, mDbDao, navControllerTrainer, mainActivity)
    }

    fun createNewClientVM(navControllerTrainer: NavHostController, mainActivity: MainActivity) {
        repoFB.createNewClient(this, navControllerTrainer, mainActivity)
    }

    fun addClientFromBDVM(navControllerTrainer: NavHostController) {
        repoFB.addClientFromBD(this, navControllerTrainer)
    }


    fun deleteClientVM(uid: String) {
        repoFB.deleteClient(uid, this, mDbDao)
    }

    fun deleteClientVMBig(uid: String) {
        repoFB.deleteClientBig(uid, this, mDbDao)
    }

    //AdminCode
    private var _adminCode = MutableLiveData("")
    var adminCode: LiveData<String> = _adminCode

    fun setAdminCode(AdminCodeString: String) {
        _adminCode.value = AdminCodeString
    }

    fun getAdminCode(): String {
        return adminCode.value.toString()
    }

    fun getCodeFromDB(function: (string: String) -> Unit) {
        repoFB.getCodeAdmin {
            function(it)
        }
    }

    private var _listTrainers = MutableLiveData<MutableList<TrainerMod>>(mutableListOf())
    var listTrainers: LiveData<MutableList<TrainerMod>> = _listTrainers
    fun setListTrainers(listTrainersStringTrainer: MutableList<TrainerMod>) {
        _listTrainers.postValue(listTrainersStringTrainer)
    }

    fun getListTrainers() {
        repoFB.getTrainersList {
            repoFB.setTrainersToRoom(this@MainViewModel, mDb = mDbDao, it)
        }
    }

    fun getUsersTrainerVM() {
        repoFB.getUsersTrainer(mDbDao, this){
            repoFB.getModelsOfUsersTrainers(it, this, mDbDao){
            }
        }
    }
    //Scheduler
    var _start = MutableLiveData("")
    var start: LiveData<String> = _start

    fun setStart(start: String) {
        _start.value = start
    }

    fun getStart(): String {
        return start.value.toString()
    }

    var _end = MutableLiveData("")
    var end: LiveData<String> = _end

    fun setEnd(end: String) {
        _end.value = end
    }

    fun getEnd(): String {
        return end.value.toString()
    }

    var _desc = MutableLiveData("")
    var desc: LiveData<String> = _desc

    fun setDesc(desc: String) {
        _desc.value = desc
    }

    fun getDesc(): String {
        return desc.value.toString()
    }

    var _dateStart = MutableLiveData<MutableList<String>>(mutableListOf())
    var dateStart: LiveData<MutableList<String>> = _dateStart

    fun setDateStart(dateStartString: MutableList<String>) {
        _dateStart.value = dateStartString
    }

    fun getDateStart(): MutableList<String> {
        return dateStart.value!!
    }
    private var _timeStart = MutableLiveData<String>()
    var timeStart: LiveData<String> = _timeStart

    fun setTimeStart(timeStartString: String) {
        _timeStart.value = timeStartString
    }

    fun getTimeStart(): String {
        return timeStart.value.toString()
    }

    var _timeEnd = MutableLiveData<String>()
    var timeEnd: LiveData<String> = _timeEnd

    fun setTimeEnd(timeEndString: String) {
        _timeEnd.value = timeEndString
    }

    fun getTimeEnd(): String {
        return timeEnd.value.toString()
    }

    val eventList = MutableLiveData<MutableList<EventModel>>(mutableListOf())
    fun getListOfEvents(function: () -> Unit) {
        repoFB.getEventsFromBD(mDb = mDbDao, this) {
            function()
        }
    }
    fun getListOfEventsForAdmin(id: String, function: () -> Unit) {
        repoFB.getAdminsTrainerRasp(id, mDb = mDbDao, this) {
            function()
        }
    }
    fun getListOfEventsUser(navControllerUser: NavHostController, mainActivity: MainActivity,  function: () -> Unit) {
        repoFB.getEventsFromBDUser( mainActivity, navControllerUser, mDb = mDbDao, this) {
            function()
        }
    }

    fun getGroupEditVM(color: Int, function: () -> Unit) {
        repoFB.getGroupEdit(this, color) {
            function()
        }
    }

    fun changeGroupVM(navControllerTrainer: NavHostController, mainActivity: MainActivity) {
        repoFB.changeGroup(
            this,
            navControllerTrainer = navControllerTrainer,
            mainActivity = mainActivity
        )

    }

    fun deleteGroup() {
        repoFB.deleteGroup(this)

    }

    fun deleteFromRaspBD(event: LocalDateTime, function: () -> Unit) {
        repoFB.deleteFromRasp(event = event, mDbDao) {
            function()

        }
    }

    fun checkUsersFor(key: String, function: (Boolean) -> Unit) {
        repoFB.checkForUser(key = key) {
            function(it)
        }
    }

    var _messages = MutableLiveData<MutableList<ModelMessage>>(mutableListOf())
    fun getMessagesVM(id: String, function: () -> Unit) {
        repoFB.getMessages(id = id, this) {
            function()
        }
    }

    fun getMessagesUserVM(id: String, trainerId: String, function: () -> Unit) {
        repoFB.getMessagesUser(id = id, this, trainerId) {
            function()
        }
    }

    fun sendMessage(who: String) {
        message.value?.let { message ->
            repoFB.sendMessage(who = who, message = message,receivingUserID = getNotifyId()) {
            }

        }
    }
fun getTrainerName(id: String, function: (name:String) -> Unit) {
    repoFB.getNameOfTrainerForNotif(id) {
        function(it)
    }
}
    fun getUserName(id: String, function: (name:String) -> Unit){
        repoFB.getNameOfUserForNotif(id){
            function(it)
        }
}
    var message = MutableLiveData("")
    fun clearChat() {
        repoFB.clearChat(getNotifyId(), this)
    }

    fun getUserKeyVM(function: (key: String) -> Unit) {
        repoFB.getUserKey() {
            function(it)
        }
    }
    var _userKey = MutableLiveData("")
    var yearsChild = MutableLiveData("")

    fun getUserDataVM(function: () -> Unit){
        repoFB.getUserData(this){
            function()
        }
    }

    fun getUserUidVM(key: String, function: (String) -> Unit){
        repoFB.getUserUid(key = key){
            function(it)
        }
    }
    var listOfColors = MutableLiveData<List<Color>>()
    var listUsersForCreateGroup = MutableLiveData<List<User>>()
    fun getListOfColors(function: (List<Color>) -> Unit){
        repoFB.getListColors {
            listOfColors.value = it
            function(it)
        }
    }
}