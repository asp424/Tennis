package com.asp424.tennis.screens.admin


import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.NODE_TRAINERS
import com.asp424.tennis.R
import com.asp424.tennis.REF_DATABASE_ROOT
import com.asp424.tennis.screens.TopBar
import com.asp424.tennis.utils.AppValueEventListener
import com.asp424.tennis.utils.asDate
import com.asp424.tennis.utils.asTime
import com.asp424.tennis.utils.getTrainerModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun Coordinates(viewModel: MainViewModel) {
    var latitude: String? by remember {
        mutableStateOf("")
    }
    var longitude: String? by remember {
        mutableStateOf("")
    }

    var time: String? by remember {
        mutableStateOf("")
    }
    var callback: Dp? by remember {
        mutableStateOf(0.dp)
    }
    val mapView = rememberMapViewWithLifecycle()
    REF_DATABASE_ROOT.child(NODE_TRAINERS).child(viewModel.getNotifyId())
        .addValueEventListener(AppValueEventListener {
            latitude = it.getTrainerModel().latitude
            longitude = it.getTrainerModel().longitude
            time = it.getTrainerModel().gps_time.toString()
            callback = 30.dp
        })
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(tittle = viewModel.getNotifyName())
    }
    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (latitude != "")
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = 52.dp, bottom = callback!!)
            ) {
                AndroidView({ mapView }) { mapView ->
                    mapView.getMapAsync { map ->
                        map.uiSettings.isZoomControlsEnabled = true
                        val pickUp = LatLng(latitude!!.toDouble(), longitude!!.toDouble())
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pickUp, 15f))
                        val markerOptions = MarkerOptions()
                            .title(time!!.asTime() + "  " + time!!.asDate())
                            .position(pickUp)
                        map.addMarker(markerOptions)
                    }
                }
            }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }
    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }