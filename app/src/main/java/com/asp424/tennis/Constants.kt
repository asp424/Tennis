package com.asp424.tennis

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

var AUTH = FirebaseAuth.getInstance()
var MESSAGING_TOKEN = FirebaseMessaging.getInstance().token
lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
var CURRENT_UID = AUTH.currentUser?.uid.toString()
var REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_TOKEN = "token"
const val CHILD_REGISTRATION_TIMESTAMP = "timeRegistrationStamp"
const val NODE_TRAINERS = "trainers"
const val NODE_USERS = "users"
const val CHILD_NAME = "name"
const val CHILD_NAME_CHILD = "name_child"
const val CHILD_SECONDNAME_CHILD = "secondName_child"
const val CHILD_YEARS_CHILD = "years_old_child"
const val CHILD_SECONDNAME = "secondName"
const val CHILD_PATRONYMIC = "patronymic"
const val CHILD_WORK_PLACE = "workPlace"
const val CHILD_EMAIL = "e_mail"
const val CHILD_WHATSAPP = "whatsapp_number"
const val CHILD_CITY = "city_of_training"
const val CHILD_CLIENTS = "clients"
const val CHILD_USER_PLAN = "plan_of_training"
const val CHILD_CLIENTS_PHONE = "phone_of_parent"
const val NODE_GROUPS = "groups"
const val CHILD_COLOR = "color"
const val CHILD_NAME_GROUP = "name_group"
const val CHILD_CODE = "code"
const val KEY = "key"

const val CHILD_TEXT = "text"

const val CHILD_FROM = "from"
const val CHILD_WHO = "who"
const val CHILD_TIMESTAMP = "timeStamp"

const val NODE_MESSAGES = "messages"
const val CHILD_SCHOOL = "school"

