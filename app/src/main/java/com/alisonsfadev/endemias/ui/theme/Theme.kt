package com.alisonsfadev.endemias.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import java.time.format.TextStyle


// =============================================================================
// COLOR SCHEMES
// =============================================================================

private val LightColorScheme = lightColorScheme(
    primary              = ColorPrimary,
    onPrimary            = ColorOnPrimary,
    primaryContainer     = ColorPrimaryContainer,
    onPrimaryContainer   = ColorOnPrimaryDark,

    secondary            = Green400,
    onSecondary          = Color.White,
    secondaryContainer   = Green50,
    onSecondaryContainer = Green800,

    tertiary             = ColorInfo,
    onTertiary           = Color.White,
    tertiaryContainer    = ColorInfoContainer,
    onTertiaryContainer  = ColorOnInfoContainer,

    error                = ColorFocoCritico,
    onError              = Color.White,
    errorContainer       = ColorFocoCriticoContainer,
    onErrorContainer     = ColorOnFocoCriticoContainer,

    background           = BackgroundLight,
    onBackground         = TextPrimaryLight,
    surface              = SurfacePrimaryLight,
    onSurface            = TextPrimaryLight,
    surfaceVariant       = SurfaceSecondaryLight,
    onSurfaceVariant     = TextSecondaryLight,
    surfaceTint          = Green600,
    outline              = DividerLight,
    outlineVariant       = Neutral50,
    inverseSurface       = SurfacePrimaryDark,
    inverseOnSurface     = TextPrimaryDark,
    inversePrimary       = Green200,
    scrim                = Color(0xFF000000),
)

private val DarkColorScheme = darkColorScheme(
    primary              = Green400,          // #639922
    onPrimary            = Green900,
    primaryContainer     = Green800,
    onPrimaryContainer   = Green100,

    secondary            = Green200,
    onSecondary          = Green900,
    secondaryContainer   = Green900,
    onSecondaryContainer = Green100,

    tertiary             = Blue200,
    onTertiary           = Blue900,
    tertiaryContainer    = ColorInfoDarkContainer,
    onTertiaryContainer  = ColorOnInfoDarkContainer,

    error                = Red400,
    onError              = Red900,
    errorContainer       = ColorFocoCriticoDarkContainer,
    onErrorContainer     = ColorOnFocoCriticoDarkContainer,

    // Superfícies baseadas no mockup — cinza neutro escuro
    background           = BackgroundDark,        // #1A1A1A
    onBackground         = TextPrimaryDark,       // #E8E8E8
    surface              = SurfacePrimaryDark,    // #2C2C2C
    onSurface            = TextPrimaryDark,
    surfaceVariant       = SurfaceSecondaryDark,  // #363636
    onSurfaceVariant     = TextSecondaryDark,     // #9A9A9A
    surfaceTint          = Green400,
    outline              = DividerDark,           // #444444
    outlineVariant       = Neutral700,
    inverseSurface       = SurfacePrimaryLight,
    inverseOnSurface     = TextPrimaryLight,
    inversePrimary       = Green600,
    scrim                = Color(0xFF000000),
)

// =============================================================================
// EXTENDED COLORS — tokens do domínio de endemias
// =============================================================================

@Immutable
data class EndemiaColors(
    val visitado: Color,
    val visitadoContainer: Color,
    val onVisitadoContainer: Color,

    val focoCritico: Color,
    val focoCriticoContainer: Color,
    val onFocoCriticoContainer: Color,

    val ausente: Color,
    val ausenteContainer: Color,
    val onAusenteContainer: Color,

    val pendente: Color,
    val pendenteContainer: Color,
    val onPendenteContainer: Color,

    val info: Color,
    val infoContainer: Color,
    val onInfoContainer: Color,

    val topBar: Color,
    val onTopBar: Color,
    val topBarSubtitle: Color,
)

val LightEndemiaColors = EndemiaColors(
    visitado            = ColorVisitado,
    visitadoContainer   = ColorVisitadoContainer,
    onVisitadoContainer = ColorOnVisitadoContainer,

    focoCritico            = ColorFocoCritico,
    focoCriticoContainer   = ColorFocoCriticoContainer,
    onFocoCriticoContainer = ColorOnFocoCriticoContainer,

    ausente            = ColorAusente,
    ausenteContainer   = ColorAusenteContainer,
    onAusenteContainer = ColorOnAusenteContainer,

    pendente            = ColorPendente,
    pendenteContainer   = ColorPendenteContainer,
    onPendenteContainer = ColorOnPendenteContainer,

    info            = ColorInfo,
    infoContainer   = ColorInfoContainer,
    onInfoContainer = ColorOnInfoContainer,

    topBar         = ColorPrimaryDark,
    onTopBar       = Color.White,
    topBarSubtitle = Green100,
)

val DarkEndemiaColors = EndemiaColors(
    visitado            = Green400,
    visitadoContainer   = Green950,
    onVisitadoContainer = Green100,

    focoCritico            = ColorFocoCriticoDark,
    focoCriticoContainer   = ColorFocoCriticoDarkContainer,
    onFocoCriticoContainer = ColorOnFocoCriticoDarkContainer,

    ausente            = ColorAusenteDark,
    ausenteContainer   = ColorAusenteDarkContainer,
    onAusenteContainer = ColorOnAusenteDarkContainer,

    pendente            = Neutral400,
    pendenteContainer   = Neutral800,
    onPendenteContainer = Neutral200,

    info            = ColorInfoDark,
    infoContainer   = ColorInfoDarkContainer,
    onInfoContainer = ColorOnInfoDarkContainer,

    topBar         = Green800,
    onTopBar       = Green100,
    topBarSubtitle = Green200,
)

val LocalEndemiaColors = staticCompositionLocalOf { LightEndemiaColors }

// =============================================================================
// TYPOGRAPHY
// =============================================================================

//val EndemiaTypography = Typography(
//    titleLarge = TextStyle(
//        fontWeight = FontWeight.Medium,
//        fontSize   = 18.sp,
//        lineHeight = 24.sp,
//    ),
//    titleMedium = TextStyle(
//        fontWeight = FontWeight.Medium,
//        fontSize   = 15.sp,
//        lineHeight = 20.sp,
//    ),
//    titleSmall = TextStyle(
//        fontWeight = FontWeight.Medium,
//        fontSize   = 13.sp,
//        lineHeight = 18.sp,
//    ),
//    bodyLarge = TextStyle(
//        fontWeight = FontWeight.Normal,
//        fontSize   = 14.sp,
//        lineHeight = 20.sp,
//    ),
//    bodyMedium = TextStyle(
//        fontWeight = FontWeight.Normal,
//        fontSize   = 13.sp,
//        lineHeight = 18.sp,
//    ),
//    bodySmall = TextStyle(
//        fontWeight = FontWeight.Normal,
//        fontSize   = 12.sp,
//        lineHeight = 16.sp,
//    ),
//    labelLarge = TextStyle(
//        fontWeight = FontWeight.Medium,
//        fontSize   = 12.sp,
//        lineHeight = 16.sp,
//    ),
//    labelMedium = TextStyle(
//        fontWeight = FontWeight.Normal,
//        fontSize   = 11.sp,
//        lineHeight = 15.sp,
//    ),
//    labelSmall = TextStyle(
//        fontWeight = FontWeight.Normal,
//        fontSize   = 10.sp,
//        lineHeight = 14.sp,
//        letterSpacing = 0.06.sp,
//    ),
//)

// =============================================================================
// SHAPES
// =============================================================================

val EndemiaShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
    small      = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    medium     = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    large      = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
)

// =============================================================================
// SPACING
// =============================================================================

@Immutable
data class EndemiaSpacing(
    val xxs: Dp =  2.dp,
    val xs:  Dp =  4.dp,
    val sm:  Dp =  6.dp,
    val md:  Dp =  8.dp,
    val lg:  Dp = 12.dp,
    val xl:  Dp = 16.dp,
    val xxl: Dp = 20.dp,
    val section: Dp = 24.dp,
)

val LocalEndemiaSpacing = compositionLocalOf { EndemiaSpacing() }

// =============================================================================
// THEME COMPOSABLE
// =============================================================================

@Composable
fun EndemiasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val endemiaColors = if (darkTheme) DarkEndemiaColors else LightEndemiaColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = endemiaColors.topBar.toArgb()
            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalEndemiaColors  provides endemiaColors,
        LocalEndemiaSpacing provides EndemiaSpacing(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = Typography,
            shapes      = EndemiaShapes,
            content     = content,
        )
    }
}

// =============================================================================
// EXTENSÕES
// =============================================================================

val MaterialTheme.endemiaColors: EndemiaColors
    @Composable get() = LocalEndemiaColors.current

val MaterialTheme.spacing: EndemiaSpacing
    @Composable get() = LocalEndemiaSpacing.current