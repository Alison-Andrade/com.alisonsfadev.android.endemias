package com.alisonsfadev.endemias.ui.theme

import androidx.compose.ui.graphics.Color


// =============================================================================
// RAMP VERDE — cor primária do app
// =============================================================================

val Green50  = Color(0xFFEAF3DE)
val Green100 = Color(0xFFC0DD97)
val Green200 = Color(0xFF97C459)
val Green400 = Color(0xFF639922)
val Green600 = Color(0xFF3B6D11)
val Green800 = Color(0xFF27500A)
val Green900 = Color(0xFF173404)
val Green950 = Color(0xFF0F1F07)

// =============================================================================
// RAMP VERMELHO — foco crítico
// =============================================================================

val Red50   = Color(0xFFFCEBEB)
val Red100  = Color(0xFFF7C1C1)
val Red200  = Color(0xFFF09595)
val Red400  = Color(0xFFE24B4A)
val Red600  = Color(0xFFA32D2D)
val Red900  = Color(0xFF501313)
val RedDark = Color(0xFF3D1414)

// =============================================================================
// RAMP ÂMBAR — ausente / alerta
// =============================================================================

val Amber50   = Color(0xFFFAEEDA)
val Amber100  = Color(0xFFFAC775)
val Amber200  = Color(0xFFEF9F27)
val Amber600  = Color(0xFF854F0B)
val Amber800  = Color(0xFF633806)
val AmberDark = Color(0xFF2E1A00)

// =============================================================================
// RAMP AZUL — amostras / laboratório
// =============================================================================

val Blue50   = Color(0xFFE6F1FB)
val Blue200  = Color(0xFF85B7EB)
val Blue400  = Color(0xFF378ADD)
val Blue600  = Color(0xFF185FA5)
val Blue800  = Color(0xFF0C447C)
val Blue900  = Color(0xFF042C53)
val BlueDark = Color(0xFF0A1E35)

// =============================================================================
// NEUTROS — baseados no mockup (cinza escuro neutro, sem tint verde)
// =============================================================================

// Tema escuro — extraídos do mockup
val Neutral950 = Color(0xFF1A1A1A)   // fundo geral da tela
val Neutral900 = Color(0xFF232323)   // fundo de seções / conteúdo
val Neutral850 = Color(0xFF2C2C2C)   // cards
val Neutral800 = Color(0xFF363636)   // superfície elevada / campos
val Neutral700 = Color(0xFF444444)   // bordas / divisores
val Neutral400 = Color(0xFF9A9A9A)   // texto secundário
val Neutral200 = Color(0xFFD4D4D4)   // texto primário
val Neutral100 = Color(0xFFE8E8E8)   // texto em destaque / títulos

// Tema claro
val Neutral10  = Color(0xFFF5F5F5)   // fundo geral claro
val Neutral20  = Color(0xFFFFFFFF)   // cards claro
val Neutral30  = Color(0xFFEEEEEE)   // superfície elevada claro
val Neutral50  = Color(0xFFE0E0E0)   // bordas claro
val Neutral600 = Color(0xFF666666)   // texto secundário claro
val Neutral900Light = Color(0xFF1A1A1A) // texto primário claro

// =============================================================================
// SUPERFÍCIES — tema escuro (mockup)
// =============================================================================

val BackgroundDark        = Neutral950   // #1A1A1A — fundo da tela
val SurfacePrimaryDark    = Neutral850   // #2C2C2C — cards
val SurfaceSecondaryDark  = Neutral800   // #363636 — campos, superfície elevada
val SurfaceTertiaryDark   = Neutral900   // #232323 — seção entre cards
val DividerDark           = Neutral700   // #444444 — bordas e separadores

// =============================================================================
// SUPERFÍCIES — tema claro
// =============================================================================

val BackgroundLight        = Neutral10        // #F5F5F5
val SurfacePrimaryLight    = Neutral20        // #FFFFFF
val SurfaceSecondaryLight  = Neutral30        // #EEEEEE
val DividerLight           = Neutral50        // #E0E0E0

// =============================================================================
// TEXTO — tema escuro
// =============================================================================

val TextPrimaryDark    = Neutral100   // #E8E8E8
val TextSecondaryDark  = Neutral400   // #9A9A9A
val TextTertiaryDark   = Neutral700   // #444444

// =============================================================================
// TEXTO — tema claro
// =============================================================================

val TextPrimaryLight    = Neutral900Light  // #1A1A1A
val TextSecondaryLight  = Neutral600       // #666666
val TextTertiaryLight   = Neutral400       // #9A9A9A

// =============================================================================
// ALIASES SEMÂNTICOS
// =============================================================================

// Primária
val ColorPrimary          = Green600     // #3B6D11
val ColorPrimaryDark      = Green800     // #27500A
val ColorPrimaryContainer = Green50      // #EAF3DE
val ColorOnPrimary        = Color.White
val ColorOnPrimaryDark    = Green100     // #C0DD97

// Visitado
val ColorVisitado              = Green400
val ColorVisitadoContainer     = Green50
val ColorOnVisitadoContainer   = Green800

// Foco crítico
val ColorFocoCritico                  = Red400
val ColorFocoCriticoContainer         = Red50
val ColorOnFocoCriticoContainer       = Red600
val ColorFocoCriticoDark              = Red400
val ColorFocoCriticoDarkContainer     = RedDark
val ColorOnFocoCriticoDarkContainer   = Red200

// Ausente
val ColorAusente                  = Amber200
val ColorAusenteContainer         = Amber50
val ColorOnAusenteContainer       = Amber800
val ColorAusenteDark              = Amber200
val ColorAusenteDarkContainer     = AmberDark
val ColorOnAusenteDarkContainer   = Amber100

// Info / lab
val ColorInfo                  = Blue400
val ColorInfoContainer         = Blue50
val ColorOnInfoContainer       = Blue800
val ColorInfoDark              = Blue400
val ColorInfoDarkContainer     = BlueDark
val ColorOnInfoDarkContainer   = Blue200

// Pendente
val ColorPendente              = Neutral400
val ColorPendenteContainer     = Neutral800
val ColorOnPendenteContainer   = Neutral200