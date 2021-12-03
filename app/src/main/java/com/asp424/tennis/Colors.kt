package com.asp424.tennis

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Blue300 = Color(0xFF0D9BDD)
val Blue200 = Color(0xFF0A88C4)
val backChat = Color(0xFF8DC9F7)

val DarkColors = darkColors(
    /*primary = Color.Blue,
    secondary = Color.Red,

    primaryVariant = Color.Cyan,
    secondaryVariant = Color.Cyan,*/
    background = Color.White,
    surface = Blue300
   /* error = Color.Red,
    onBackground = Color.Cyan,
    onSurface = Color.White,*/
    )

val LightColors = lightColors(
    surface = Blue300,
    background = Color.Magenta,
   /* primary = Color.White,
    onPrimary = Color.Black,
    secondary = Color.Magenta,

    primaryVariant = Color.Magenta,
    secondaryVariant = Color.Magenta,

    error = Color.Magenta,
    onSecondary = Color.Magenta,
    onBackground = Color.Magenta,
    onSurface = Color.Magenta,
    onError = Color.Magenta*/
)


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

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

