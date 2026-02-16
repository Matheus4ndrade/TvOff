package com.matheus.tvoff.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.matheus.tvoff.ui.componentes.SeletorAlbunsDialog
import com.matheus.tvoff.ui.registro.RegistroDialog
import com.matheus.tvoff.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen(
    nickname: String,
    aoVoltarHome: () -> Unit,
    viewModel: PerfilViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()
    val scope = rememberCoroutineScope()

    var mostrarDialogRegistro by remember { mutableStateOf(false) }
    var mostrarSeletorAlbuns by remember { mutableStateOf(false) }

    LaunchedEffect(nickname) {
        viewModel.carregarPerfil(nickname)
    }

    LaunchedEffect(estado) {
        if (estado is EstadoPerfil.NaoExiste) {
            mostrarDialogRegistro = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PretoBase)
    ) {
        when (val estadoAtual = estado) {
            is EstadoPerfil.Carregando -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AzulTurquesa
                )
            }

            is EstadoPerfil.Erro -> {
                TelaErro(
                    mensagem = estadoAtual.mensagem,
                    aoTentarNovamente = { viewModel.carregarPerfil(nickname) }
                )
            }

            is EstadoPerfil.Sucesso -> {
                TelaPerfil(
                    perfil = estadoAtual.perfil,
                    aoVoltarHome = aoVoltarHome,
                    aoClicarSlot = {
                        mostrarSeletorAlbuns = true
                    }
                )
            }

            else -> {}
        }
    }

    if (mostrarDialogRegistro) {
        RegistroDialog(
            nickname = nickname,
            aoFechar = {
                mostrarDialogRegistro = false
                aoVoltarHome()
            },
            aoCriarPerfil = { perfil, senha ->
                val sucesso = viewModel.criarPerfil(perfil, senha)
                if (sucesso) {
                    mostrarDialogRegistro = false
                    mostrarSeletorAlbuns = true
                }
            }
        )
    }

    if (mostrarSeletorAlbuns) {
        val perfilAtual = (estado as? EstadoPerfil.Sucesso)?.perfil

        SeletorAlbunsDialog(
            albunsJaSelecionados = perfilAtual?.albensFavoritos ?: emptyList(),
            maxSelecao = 5,
            aoConfirmar = { albumsSelecionados ->
                scope.launch {
                    viewModel.atualizarAlbensFavoritos(albumsSelecionados)
                    mostrarSeletorAlbuns = false
                }
            },
            aoFechar = {
                mostrarSeletorAlbuns = false
            }
        )
    }
}

@Composable
private fun BoxScope.TelaErro(
    mensagem: String,
    aoTentarNovamente: () -> Unit
) {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "⚠️ Erro ao carregar perfil",
            style = MaterialTheme.typography.headlineSmall,
            color = AvaliacaoAmei
        )
        Text(
            text = mensagem,
            style = MaterialTheme.typography.bodyMedium,
            color = Cinza600
        )
        Button(
            onClick = aoTentarNovamente,
            colors = ButtonDefaults.buttonColors(containerColor = CoralVermelho)
        ) {
            Text("Tentar novamente")
        }
    }
}

@Composable
private fun TelaPerfil(
    perfil: com.matheus.tvoff.data.modelo.Perfil,
    aoVoltarHome: () -> Unit,
    aoClicarSlot: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CabecalhoPerfil(
            nome = perfil.nome,
            nickname = perfil.nickname,
            bio = perfil.bio,
            totalFavoritos = perfil.albensFavoritos.size,
            totalOuvidos = perfil.albensOuvidos.size
        )

        Spacer(modifier = Modifier.height(32.dp))

        SecaoAlbensFavoritos(
            albuns = perfil.albensFavoritos,
            aoClicarSlot = aoClicarSlot
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(onClick = aoVoltarHome) {
            Text("← Voltar para Home")
        }
    }
}

@Composable
private fun CabecalhoPerfil(
    nome: String,
    nickname: String,
    bio: String,
    totalFavoritos: Int,
    totalOuvidos: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(CoralVermelho, AzulTurquesa)
                    ),
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nome.take(2).uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                color = BrancoTotal
            )
        }

        Text(
            text = nome,
            style = MaterialTheme.typography.headlineMedium,
            color = BrancoTotal
        )

        Text(
            text = "@$nickname",
            style = MaterialTheme.typography.bodyMedium,
            color = Cinza600
        )

        if (bio.isNotBlank()) {
            Text(
                text = bio,
                style = MaterialTheme.typography.bodyMedium,
                color = Cinza700,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            EstatisticaPerfil(rotulo = "Favoritos", valor = totalFavoritos)
            EstatisticaPerfil(rotulo = "Ouvidos", valor = totalOuvidos)
        }
    }
}

@Composable
private fun EstatisticaPerfil(rotulo: String, valor: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = valor.toString(),
            style = MaterialTheme.typography.headlineSmall,
            color = BrancoTotal
        )
        Text(
            text = rotulo,
            style = MaterialTheme.typography.bodySmall,
            color = Cinza600
        )
    }
}

@Composable
private fun SecaoAlbensFavoritos(
    albuns: List<com.matheus.tvoff.data.modelo.Album>,
    aoClicarSlot: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "TOP 5 ÁLBUNS",
            style = MaterialTheme.typography.titleMedium,
            color = CoralVermelho
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            for (posicao in 0 until 5) {
                val album = albuns.getOrNull(posicao)

                Card(
                    onClick = { aoClicarSlot(posicao) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (album == null)
                            Cinza200.copy(alpha = 0.3f)
                        else
                            Cinza200
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "#${posicao + 1}",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (album == null) Cinza500 else CoralVermelho
                        )

                        if (album != null) {
                            Column {
                                Text(
                                    text = album.titulo,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = BrancoTotal
                                )
                                Text(
                                    text = album.artista,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Cinza600
                                )
                            }
                        } else {
                            Text(
                                text = "Toque para adicionar",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Cinza500
                            )
                        }
                    }
                }
            }
        }
    }
}