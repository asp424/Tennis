package com.asp424.tennis.screens.trainer

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tennis.Blue300
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.models.EventModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun BasicEvent(
    event: EventModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(end = 2.dp, bottom = 2.dp)
            .background(event.color, shape = RoundedCornerShape(4.dp))
            .padding(4.dp)
    ) {
        Text(
            text = "${event.start.format(HourFormatter)} - ${event.end.format(HourFormatter)}",
            style = TextStyle(
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                color = when (event.color) {
                    Color(0xFFE9E3E3) -> Color(0xFF000000)
                    Color(0xFFFFEB3B) -> Color(0xFF2A0768)
                    else -> Color(0xFFFFFFFF)
                }
            ),
        )
        Text(
            text = event.name,
            style = TextStyle(
                color = when (event.color) {
                    Color(0xFFE9E3E3) -> Color(0xFF000000)
                    Color(0xFFFFEB3B) -> Color(0xFF2A0768)
                    else -> Color(0xFFFFFFFF)
                }, fontStyle = FontStyle.Italic
            ),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        if (event.desc != null) {
            Text(
                text = event.desc,
                style = TextStyle(
                    color = when (event.color) {
                        Color(0xFFE9E3E3) -> Color(0xFF000000)
                        Color(0xFFFFEB3B) -> Color(0xFF2A0768)
                        else -> Color(0xFFFFFFFF)
                    }, fontStyle = FontStyle.Italic
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private class EventDataModifier(
    val event: EventModel,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = event
}

private fun Modifier.eventData(event: EventModel) = this.then(EventDataModifier(event))
private val DayFormatter = DateTimeFormatter.ofPattern("EE, d MMM")

@Composable
fun BasicDayHeader(
    day: LocalDate,
    modifier: Modifier = Modifier,
) {
    Text(
        style = TextStyle(
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            color = if (isSystemInDarkTheme()) Color.White else Color.Blue
        ),
        text = day.format(DayFormatter),
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
}

@Composable
fun ScheduleHeader(
    minDate: LocalDate,
    maxDate: LocalDate,
    dayWidth: Dp,
    modifier: Modifier = Modifier,
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it) },
) {
    Row(modifier = modifier) {
        val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
        repeat(numDays) { i ->
            Box(modifier = Modifier.width(dayWidth)) {
                dayHeader(minDate.plusDays(i.toLong()))
            }
        }
    }
}

private val HourFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun BasicSidebarLabel(
    time: LocalTime,
    modifier: Modifier = Modifier,
) {
    Text(
        style = TextStyle(
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            color = if (isSystemInDarkTheme()) Color.White else Color.Blue
        ),
        text = time.format(HourFormatter),
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
    )
}

@Composable
fun ScheduleSidebar(
    state: TransformableState,
    scale: Float,
    hourHeight: Dp,
    modifier: Modifier = Modifier,
    label: @Composable (time: LocalTime) -> Unit = { BasicSidebarLabel(time = it) },
) {
    Column(
        modifier = modifier.graphicsLayer(

            scaleY = scale,
            )
            // add transformable to listen to multitouch transformation events
            // after offset
            .transformable(state = state)
    ) {
        val startTime = LocalTime.of(8, 0)
        repeat(18) { i ->
            Box(modifier = Modifier.height(hourHeight)) {
                label(startTime.plusHours(i.toLong()))
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Schedule(
    state: TransformableState,
    scale: Float,
    events: List<EventModel>,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: EventModel) -> Unit = { BasicEvent(event = it) },
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it) },
    minDate: LocalDate = events.minByOrNull(EventModel::start)?.start?.toLocalDate()
        ?: LocalDate.now(),
    maxDate: LocalDate = events.maxByOrNull(EventModel::end)?.end?.toLocalDate() ?: LocalDate.now()
) {
    val dayWidth = 256.dp
    val hourHeight = 64.dp
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    var sidebarWidth by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        ScheduleHeader(
            minDate = minDate,
            maxDate = maxDate,
            dayWidth = dayWidth,
            dayHeader = dayHeader,
            modifier = Modifier
                .padding(start = with(LocalDensity.current) { sidebarWidth.toDp() })
                .horizontalScroll(horizontalScrollState)
        )
        Row(modifier = Modifier.weight(1f)) {
            ScheduleSidebar(
                scale = scale,
                state = state,
                hourHeight = hourHeight,
                modifier = Modifier
                    .verticalScroll(verticalScrollState)
                    .onGloballyPositioned { sidebarWidth = it.size.width }
            )
            BasicSchedule(
                scale = scale,
                state = state,
                events = events,
                eventContent = eventContent,
                dayWidth = dayWidth,
                hourHeight = hourHeight,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState),
                viewModel = viewModel
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun BasicSchedule(
    state: TransformableState,
    scale: Float,
    events: List<EventModel>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: EventModel) -> Unit = { BasicEvent(event = it) },
    minDate: LocalDate = events.minByOrNull(EventModel::start)!!.start.toLocalDate(),
    maxDate: LocalDate = events.maxByOrNull(EventModel::end)!!.end.toLocalDate(),
    dayWidth: Dp,
    hourHeight: Dp,
    viewModel: MainViewModel
) {

    val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
    val dividerColor = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    Layout(
        content = {
            events.sortedBy(EventModel::start).forEach { event ->
                var visible by remember { mutableStateOf(false) }
                var visible1 by remember { mutableStateOf(true) }
                val density = LocalDensity.current
                Box(modifier = Modifier
                    .eventData(event)
                    .clickable {
                        visible = !visible
                    }
                ) {
                    AnimatedVisibility(
                        visible = visible1,
                        enter = slideInVertically(
                            initialOffsetY = { with(density) { -40.dp.roundToPx() } }
                        ) + expandVertically(
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {
                        eventContent(event)
                        AnimatedVisibility(
                            visible = visible,
                            enter = slideInVertically(
                                initialOffsetY = { with(density) { -40.dp.roundToPx() } }
                            ) + expandVertically(
                                expandFrom = Alignment.Top
                            ) + fadeIn(
                                initialAlpha = 0.3f
                            ),
                            exit = slideOutVertically() + shrinkVertically() + fadeOut()
                        ) {
                            Card(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(start = 40.dp, bottom = 6.dp, top = 2.dp)
                                    .shadow(20.dp),
                                border = BorderStroke(
                                    width = 2.dp, color = when (event.color.toString()) {
                                        "1" -> Color.Red
                                        "2" -> Blue300
                                        "3" -> Color.Green
                                        "4" -> Color.Yellow
                                        else -> Color.White
                                    }
                                ), elevation = 20.dp
                            ) {
                                Column {
                                    Text(
                                        text = "Удалить из расписания",
                                        style = TextStyle(
                                            fontSize = 17.sp,
                                            textAlign = TextAlign.Center,
                                            fontStyle = FontStyle.Italic,
                                            color = if (isSystemInDarkTheme()) Color.White else Color.Blue
                                        ),
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .clickable {
                                                viewModel.deleteFromRaspBD(event = event.start) {
                                                    visible1 = !visible1
                                                }
                                                visible = false
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier.graphicsLayer(
            scaleY = scale,
        ).transformable(state = state)
            .drawBehind {
                repeat(18) {
                    drawLine(
                        dividerColor,
                        start = Offset(0f, (it + 1) * hourHeight.toPx()),
                        end = Offset(size.width, (it + 1) * hourHeight.toPx()),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                repeat(numDays - 1) {
                    drawLine(
                        dividerColor,
                        start = Offset((it + 1) * dayWidth.toPx(), 0f),
                        end = Offset((it + 1) * dayWidth.toPx(), size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    ) { measurables, constraints ->
        val height = hourHeight.roundToPx() * 18
        val width = dayWidth.roundToPx() * numDays
        val placeablesWithEvents = measurables.map { measurable ->
            val event = measurable.parentData as EventModel
            val eventDurationMinutes = ChronoUnit.MINUTES.between(event.start, event.end)
            val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = dayWidth.roundToPx(),
                    maxWidth = dayWidth.roundToPx(),
                    minHeight = eventHeight,
                    maxHeight = eventHeight
                )
            )
            Pair(placeable, event)
        }
        layout(width, height) {
            placeablesWithEvents.forEach { (placeable, event) ->
                val eventOffsetMinutes =
                    ChronoUnit.MINUTES.between(LocalTime.of(8, 0), event.start.toLocalTime())
                val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                val eventOffsetDays =
                    ChronoUnit.DAYS.between(minDate, event.start.toLocalDate()).toInt()
                val eventX = eventOffsetDays * dayWidth.roundToPx()
                placeable.place(eventX, eventY)
            }
        }
    }
}

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)
val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

@Composable
fun WeekScheduleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

