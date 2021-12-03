package com.asp424.tennis.screens.user


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tennis.MainViewModel
import com.asp424.tennis.models.EventModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun BasicEventUser(
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
fun BasicDayHeaderUser(
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
fun ScheduleHeaderUser(
    minDate: LocalDate,
    maxDate: LocalDate,
    dayWidth: Dp,
    modifier: Modifier = Modifier,
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeaderUser(day = it) },
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
fun BasicSidebarLabelUser(
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
fun ScheduleSidebarUser(
    state: TransformableState,
    scale: Float,
    hourHeight: Dp,
    modifier: Modifier = Modifier,
    label: @Composable (time: LocalTime) -> Unit = { BasicSidebarLabelUser(time = it) },
) {
    Column(modifier = modifier.graphicsLayer(
        scaleY = scale,
    )
        .transformable(state = state)) {
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
fun ScheduleUser(
    state: TransformableState,
    scale: Float,
    events: List<EventModel>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: EventModel) -> Unit = { BasicEventUser(event = it) },
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeaderUser(day = it) },
    minDate: LocalDate = events.minByOrNull(EventModel::start)?.start?.toLocalDate() ?: LocalDate.now(),
    maxDate: LocalDate = events.maxByOrNull(EventModel::end)?.end?.toLocalDate() ?: LocalDate.now()
) {
    val dayWidth = 256.dp
    val hourHeight = 64.dp
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    var sidebarWidth by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        ScheduleHeaderUser(
            minDate = minDate,
            maxDate = maxDate,
            dayWidth = dayWidth,
            dayHeader = dayHeader,
            modifier = Modifier
                .padding(start = with(LocalDensity.current) { sidebarWidth.toDp() })
                .horizontalScroll(horizontalScrollState)
        )
        Row(modifier = Modifier.weight(1f)) {
            ScheduleSidebarUser(
                scale = scale,
                state = state,
                hourHeight = hourHeight,
                modifier = Modifier
                    .verticalScroll(verticalScrollState)
                    .onGloballyPositioned { sidebarWidth = it.size.width }
            )
            BasicScheduleUser(
                scale = scale,
                state = state,
                events = events,
                eventContent = eventContent,
                dayWidth = dayWidth,
                hourHeight = hourHeight,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun BasicScheduleUser(
    state: TransformableState,
    scale: Float,
    events: List<EventModel>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: EventModel) -> Unit = { BasicEventUser(event = it) },
    minDate: LocalDate = events.minByOrNull(EventModel::start)!!.start.toLocalDate(),
    maxDate: LocalDate = events.maxByOrNull(EventModel::end)!!.end.toLocalDate(),
    dayWidth: Dp,
    hourHeight: Dp
) {
    val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
    val dividerColor = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    Layout(
        content = {
            events.sortedBy(EventModel::start).forEach { event ->
                Box(modifier = Modifier
                    .eventData(event)
                    .clickable {}
                ) {
                    eventContent(event)
                }
            }
        },
        modifier = modifier.graphicsLayer(
            scaleY = scale,
        )
            .transformable(state = state)
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

