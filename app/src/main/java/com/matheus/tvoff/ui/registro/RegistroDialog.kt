package com.matheus.tvoff.ui.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.matheus.tvoff.data.modelo.Perfil
import com.matheus.tvoff.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun RegistroDialog(
    nickname: String,
    aoFechar: () -> Unit,
    aoCriarPerfil: suspend (Perfil, String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }
    var criando by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = aoFechar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PretoBase.copy(alpha = 0.95f))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PretoSecundario)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    CabecalhoRegistro(nickname)

                    Spacer(modifier = Modifier.height(8.dp))

                    CamposFormulario(
                        nome = nome,
                        aoMudarNome = { nome = it },
                        senha = senha,
                        aoMudarSenha = { senha = it },
                        confirmarSenha = confirmarSenha,
                        aoMudarConfirmacao = { confirmarSenha = it },
                        bio = bio,
                        aoMudarBio = { if (it.length <= 200) bio = it },
                        habilitado = !criando
                    )

                    mensagemErro?.let { MensagemErro(it) }
                    if (criando) IndicadorCarregando()

                    Spacer(modifier = Modifier.height(8.dp))

                    BotoesAcao(
                        habilitado = !criando,
                        aoFechar = aoFechar,
                        aoCriar = {
                            mensagemErro = validarCampos(nome, senha, confirmarSenha)

                            if (mensagemErro == null) {
                                criando = true

                                val perfil = Perfil(
                                    nickname = nickname.lowercase().trim(),
                                    nome = nome.trim(),
                                    bio = bio.trim(),
                                    avatarUrl = null,
                                    albensFavoritos = emptyList(),
                                    albensOuvidos = emptyList()
                                )

                                scope.launch {
                                    try {
                                        aoCriarPerfil(perfil, senha)
                                    } catch (e: Exception) {
                                        mensagemErro = "Erro ao criar perfil"
                                        criando = false
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CabecalhoRegistro(nickname: String) {
    Text(
        text = "✦ Criar Perfil",
        style = MaterialTheme.typography.headlineMedium,
        color = BrancoTotal
    )
    Text(
        text = "O nickname @$nickname está disponível!",
        style = MaterialTheme.typography.bodyMedium,
        color = AzulTurquesa
    )
}

@Composable
private fun CamposFormulario(
    nome: String,
    aoMudarNome: (String) -> Unit,
    senha: String,
    aoMudarSenha: (String) -> Unit,
    confirmarSenha: String,
    aoMudarConfirmacao: (String) -> Unit,
    bio: String,
    aoMudarBio: (String) -> Unit,
    habilitado: Boolean
) {
    val coresCampo = OutlinedTextFieldDefaults.colors(
        focusedTextColor = BrancoTotal,
        unfocusedTextColor = BrancoTotal,
        focusedBorderColor = AzulTurquesa,
        unfocusedBorderColor = Cinza400,
        focusedLabelColor = AzulTurquesa,
        unfocusedLabelColor = Cinza600
    )

    OutlinedTextField(
        value = nome,
        onValueChange = aoMudarNome,
        label = { Text("Nome completo") },
        modifier = Modifier.fillMaxWidth(),
        colors = coresCampo,
        singleLine = true,
        enabled = habilitado
    )

    OutlinedTextField(
        value = senha,
        onValueChange = aoMudarSenha,
        label = { Text("Senha") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = coresCampo,
        singleLine = true,
        enabled = habilitado
    )

    OutlinedTextField(
        value = confirmarSenha,
        onValueChange = aoMudarConfirmacao,
        label = { Text("Confirmar senha") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = coresCampo,
        singleLine = true,
        enabled = habilitado
    )

    OutlinedTextField(
        value = bio,
        onValueChange = aoMudarBio,
        label = { Text("Bio (opcional)") },
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = coresCampo,
        maxLines = 4,
        supportingText = {
            Text(
                text = "${bio.length}/200",
                color = Cinza600,
                style = MaterialTheme.typography.bodySmall
            )
        },
        enabled = habilitado
    )
}

@Composable
private fun MensagemErro(mensagem: String) {
    Text(
        text = "⚠ $mensagem",
        color = AvaliacaoAmei,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun IndicadorCarregando() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            color = AzulTurquesa,
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Criando perfil...",
            color = Cinza600,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun BotoesAcao(
    habilitado: Boolean,
    aoFechar: () -> Unit,
    aoCriar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = aoFechar,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Cinza700),
            enabled = habilitado
        ) {
            Text("Cancelar")
        }

        Button(
            onClick = aoCriar,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = CoralVermelho),
            enabled = habilitado
        ) {
            Text(if (habilitado) "Criar Perfil" else "Criando...")
        }
    }
}

private fun validarCampos(nome: String, senha: String, confirmarSenha: String): String? {
    return when {
        nome.isBlank() -> "Digite seu nome completo"
        senha.length < 6 -> "Senha deve ter no mínimo 6 caracteres"
        senha != confirmarSenha -> "As senhas não coincidem"
        else -> null
    }
}