package com.matheus.tvoff.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matheus.tvoff.ui.theme.*

@Composable
fun PerfilScreen(
    nickname: String,
    aoVoltarHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PretoBase),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Perfil: @$nickname",
                style = MaterialTheme.typography.headlineLarge,
                color = BrancoTotal
            )
            
            Text(
                text = "Tela de perfil em construção...",
                style = MaterialTheme.typography.bodyLarge,
                color = Cinza600
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = aoVoltarHome,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoralVermelho
                )
            ) {
                Text("← Voltar para Home")
            }
        }
    }
}
