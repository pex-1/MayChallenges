package com.example.maychallenges.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.maychallenges.R

private val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

val DmSansFamily: FontFamily = FontFamily(
    Font(googleFont = GoogleFont("DM Sans"), fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("DM Sans"), fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("DM Sans"), fontProvider = fontProvider, weight = FontWeight.SemiBold),
)

val TitleStyle = TextStyle(
    fontFamily = DmSansFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp,
    lineHeight = 26.sp,
)

val SubtitleStyle = TextStyle(
    fontFamily = DmSansFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 21.sp,
)

val BodyStyle = TextStyle(
    fontFamily = DmSansFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
)

val LabelStyle = TextStyle(
    fontFamily = DmSansFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
)

val Typography = Typography(
    headlineMedium = TitleStyle,
    titleMedium = SubtitleStyle,
    bodyMedium = BodyStyle,
    labelMedium = LabelStyle,
)
