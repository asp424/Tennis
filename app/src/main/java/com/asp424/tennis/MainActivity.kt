package com.asp424.tennis

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.asp424.tennis.permissions.*
import com.asp424.tennis.screens.admin.ScreenAdmin
import com.asp424.tennis.screens.admin.ScreenEnterCodeAdmin
import com.asp424.tennis.screens.change_profile.ChangeScreen
import com.asp424.tennis.screens.trainer.EnterDataTrainer
import com.asp424.tennis.screens.trainer.TrainerScreen
import com.asp424.tennis.screens.user.EnterDataBigUser
import com.asp424.tennis.screens.user.EnterDataUser
import com.asp424.tennis.screens.user.UserScreen
import com.asp424.tennis.utils.appData
import com.asp424.tennis.utils.getStateScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: MainViewModel by viewModels {
            MainViewModelFactory(appData)
        }

        intent(vm)
        if (AUTH.currentUser?.uid != null) {
            when (getStateScreen(mainActivity = this)) {
                5 -> { if (checkPermissions(GPS, this, reqCode = PERMISSION_REQUEST)
                        && checkPermissions(GPS_1, this, reqCode = PERMISSION_REQUEST1)
                    ) setContent { TrainerScreen(viewModel = vm, id = "") } }
                6 -> setContent { UserScreen(viewModel = vm, "") }
                3 -> { if (checkPermissions(GPS, this, reqCode = PERMISSION_REQUEST)
                        && checkPermissions(GPS_1, this, reqCode = PERMISSION_REQUEST1)
                    ) setContent { EnterDataTrainer(viewModel = vm) } }
                4 -> setContent { EnterDataUser(
                    viewModel = vm
                )
                }
                12 -> { setContent { ScreenAdmin(viewModel = vm, id = "") }
                }
                11 -> { setContent { ScreenEnterCodeAdmin(viewModel = vm) } }
                22 -> { setContent { EnterDataBigUser(viewModel = vm) } }
                else -> setContent { ChangeScreen(viewModel = vm) }
            }
        } else setContent { ChangeScreen(viewModel = vm) }
    }
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
private fun intent(vm: MainViewModel) {
    if (intent?.action == "CLICK_ACTION") {
        val id = intent.extras!!["userUid"].toString()
        if (getStateScreen(mainActivity = this) == 6)
        {
            vm.getMessagesUserVM(CURRENT_UID, id){
                setContent {
                    UserScreen(viewModel = vm, id = id)
                }
            }
        }
        if (getStateScreen(mainActivity = this) == 5)
            vm.getMessagesVM(id){
                setContent {
                    TrainerScreen(viewModel = vm, id)
                }
            }
        if (getStateScreen(mainActivity = this) == 12)
            vm.getMessagesVM(id){
                setContent {
                    ScreenAdmin(viewModel = vm, id)
                }
            }
    }


}
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PERMISSION_REQUEST)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    checkPermissions(
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        mainActivity = this, reqCode = PERMISSION_REQUEST1
                    )
                }
        } else finish()
    }
}






