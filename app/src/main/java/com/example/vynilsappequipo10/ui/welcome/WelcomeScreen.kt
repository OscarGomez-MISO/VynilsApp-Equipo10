package com.example.vynilsappequipo10.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangeEnd
import com.example.vynilsappequipo10.ui.theme.ColorOrangeStart
import com.example.vynilsappequipo10.ui.theme.ColorPinkAccent
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint
import com.example.vynilsappequipo10.ui.theme.VynilsTheme

@Composable
fun WelcomeScreen(
    onCollectorEntry: () -> Unit,
    onVisitorEntry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))

            // Logo
            Text(
                text = "Vinilos",
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontSize = 52.sp,
                color = Color.White
            )

            // Pink accent line
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(ColorPinkAccent)
            )

            Spacer(Modifier.height(28.dp))

            // Main heading
            Text(
                text = "Tu bóveda sonora te espera",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(Modifier.height(12.dp))

            // Subtitle
            Text(
                text = "THE EDITORIAL COLLECTOR",
                fontSize = 11.sp,
                color = ColorTextHint,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(52.dp))

            // Primary CTA: gradient pill button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(ColorOrangeStart, ColorOrangeEnd)
                        )
                    )
                    .clickable { onCollectorEntry() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ENTRAR COMO COLECCIONISTA  →",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            // Secondary button
            Button(
                onClick = onVisitorEntry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorSurface,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "EXPLORAR COMO VISITANTE",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    VynilsTheme {
        WelcomeScreen(onCollectorEntry = {}, onVisitorEntry = {})
    }
}
