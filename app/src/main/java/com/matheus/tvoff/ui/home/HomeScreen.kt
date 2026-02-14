package com.matheus.tvoff.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matheus.tvoff.ui.theme.*

/**
 * add na tela inicial
 * carrossel de albuns
 */

@Composable
fun HomeScreen(
    aoClicarEntrar: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PretoBase)
    ) {

        GridPattern()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            LogoAnimada()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SEU GOSTO MUSICAL EM UM SÓ LUGAR",
                style = MaterialTheme.typography.labelLarge,
                color = BrancoTotal,
                letterSpacing = 2.5.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            FormularioUsername(
                username = username,
                aoMudarUsername = { username = it },
                aoClicarEntrar = { aoClicarEntrar(username) }
            )

            Spacer(modifier = Modifier.height(80.dp))
            GridFeatures()
            Spacer(modifier = Modifier.height(80.dp))
            FooterHome()
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun GridPattern() {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val gridSize = 60.dp.toPx()
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = Color.White.copy(alpha = 0.02f),
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += gridSize
        }
        var y = 0f
        while (y < size.height) {
            drawLine(
                color = Color.White.copy(alpha = 0.02f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += gridSize
        }
    }
}

@Composable
fun LogoAnimada() {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientShift"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TVOFF",
            style = MaterialTheme.typography.displayLarge.copy(
                brush = Brush.linearGradient(
                    colors = listOf(
                        CoralVermelho,
                        AzulTurquesa,
                        AmareloClaro,
                        VerdeAgua
                    ),
                    start = Offset(offsetX * 1000, offsetX * 1000),
                    end = Offset((1 - offsetX) * 1000, (1 - offsetX) * 1000)
                )
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .width(180.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            CoralVermelho,
                            AzulTurquesa,
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
fun FormularioUsername(
    username: String,
    aoMudarUsername: (String) -> Unit,
    aoClicarEntrar: () -> Unit
) {
    Row(
        modifier = Modifier
            .widthIn(max = 540.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = aoMudarUsername,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = "Digite seu username",
                    color = Cinza600
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = BrancoTotal,
                unfocusedTextColor = BrancoTotal,
                focusedBorderColor = AzulTurquesa,
                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                cursorColor = AzulTurquesa,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(
                onGo = { if (username.isNotBlank()) aoClicarEntrar() }
            )
        )

        Button(
            onClick = aoClicarEntrar,
            enabled = username.isNotBlank(),
            modifier = Modifier.height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CoralVermelho,
                disabledContainerColor = Cinza300
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 10.dp
            )
        ) {
            Text(
                text = "ENTRAR",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
fun GridFeatures() {
    Column(
        modifier = Modifier.widthIn(max = 900.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        FeatureCard(
            icone = "🎵",
            titulo = "Seu perfil musical",
            descricao = "App voltado para albuns de musica"
        )

        FeatureCard(
            icone = "⭐",
            titulo = "Os meus 5 favoritos",
            descricao = "Os discos que mais definem seu gosto"
        )

        FeatureCard(
            icone = "🎧",
            titulo = "Histórico",
            descricao = "Registre tudo que já ouviu e avalie"
        )
    }
}

@Composable
fun FeatureCard(
    icone: String,
    titulo: String,
    descricao: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.02f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icone,
                style = MaterialTheme.typography.displayMedium,
                fontSize = 40.sp
            )

            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = BrancoTotal,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Cinza600,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
fun FooterHome() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 32.dp)
    ) {
        Divider(
            color = Color.White.copy(alpha = 0.07f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        Text(
            text = "Desenvolvido por Matheus de Andrade",
            style = MaterialTheme.typography.bodySmall,
            color = Cinza400,
            textAlign = TextAlign.Center
        )
    }
}
