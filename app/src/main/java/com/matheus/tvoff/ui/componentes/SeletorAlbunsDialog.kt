package com.matheus.tvoff.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.matheus.tvoff.data.modelo.Album
import com.matheus.tvoff.data.repositorio.AlbumRepositorio
import com.matheus.tvoff.ui.theme.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeletorAlbunsDialog(
    albunsJaSelecionados: List<Album> = emptyList(),
    maxSelecao: Int = 5,
    aoConfirmar: (List<Album>) -> Unit,
    aoFechar: () -> Unit
) {
    var termoBusca by remember { mutableStateOf("") }
    var selecionados by remember { mutableStateOf(albunsJaSelecionados.toMutableList()) }
    var albunsExibidos by remember { mutableStateOf<List<Album>>(emptyList()) }
    var carregando by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val repositorio = remember { AlbumRepositorio() }
    var jobBusca: Job? = null

    LaunchedEffect(Unit) {
        carregando = true
        albunsExibidos = repositorio.obterAlbunsPopulares()
        carregando = false
    }

    LaunchedEffect(termoBusca) {
        jobBusca?.cancel()

        if (termoBusca.isBlank()) {
            carregando = true
            albunsExibidos = repositorio.obterAlbunsPopulares()
            carregando = false
        } else {
            jobBusca = scope.launch {
                delay(500)
                carregando = true
                albunsExibidos = repositorio.buscar(termoBusca)
                carregando = false
            }
        }
    }

    Dialog(
        onDismissRequest = aoFechar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PretoBase.copy(alpha = 0.95f))
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PretoSecundario)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    CabecalhoSeletor(
                        quantidadeSelecionados = selecionados.size,
                        maxSelecao = maxSelecao,
                        aoFechar = aoFechar
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CampoBusca(
                        valor = termoBusca,
                        aoMudar = { termoBusca = it },
                        carregando = carregando
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GridAlbuns(
                        albuns = albunsExibidos,
                        selecionados = selecionados,
                        carregando = carregando,
                        termoBusca = termoBusca,
                        maxSelecao = maxSelecao,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BotoesAcao(
                        quantidadeSelecionados = selecionados.size,
                        aoConfirmar = { aoConfirmar(selecionados) },
                        aoCancelar = aoFechar
                    )
                }
            }
        }
    }
}

@Composable
private fun CabecalhoSeletor(
    quantidadeSelecionados: Int,
    maxSelecao: Int,
    aoFechar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Escolha seus álbuns",
                style = MaterialTheme.typography.headlineSmall,
                color = BrancoTotal
            )
            Text(
                text = "$quantidadeSelecionados/$maxSelecao selecionados",
                style = MaterialTheme.typography.bodyMedium,
                color = if (quantidadeSelecionados >= maxSelecao) AvaliacaoAmei else Cinza600
            )
        }

        IconButton(onClick = aoFechar) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fechar",
                tint = Cinza600
            )
        }
    }
}

@Composable
private fun CampoBusca(
    valor: String,
    aoMudar: (String) -> Unit,
    carregando: Boolean
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Buscar álbum ou artista...") },
        leadingIcon = {
            if (carregando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = AzulTurquesa
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Cinza600
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = BrancoTotal,
            unfocusedTextColor = BrancoTotal,
            focusedBorderColor = AzulTurquesa,
            unfocusedBorderColor = Cinza400,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
private fun GridAlbuns(
    albuns: List<Album>,
    selecionados: MutableList<Album>,
    carregando: Boolean,
    termoBusca: String,
    maxSelecao: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when {
            albuns.isEmpty() && !carregando -> {
                MensagemVazia(termoBusca)
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 120.dp),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(albuns, key = { it.id }) { album ->
                        AlbumCard(
                            album = album,
                            estaSelecionado = selecionados.any { it.id == album.id },
                            aoClicar = {
                                if (selecionados.any { it.id == album.id }) {
                                    selecionados.removeIf { it.id == album.id }
                                } else if (selecionados.size < maxSelecao) {
                                    selecionados.add(album)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MensagemVazia(termoBusca: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🔍",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhum álbum encontrado",
            style = MaterialTheme.typography.bodyLarge,
            color = Cinza600
        )
        if (termoBusca.isNotBlank()) {
            Text(
                text = "Tente buscar por outro termo",
                style = MaterialTheme.typography.bodySmall,
                color = Cinza500
            )
        }
    }
}

@Composable
private fun BotoesAcao(
    quantidadeSelecionados: Int,
    aoConfirmar: () -> Unit,
    aoCancelar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = aoCancelar,
            modifier = Modifier.weight(1f)
        ) {
            Text("Cancelar")
        }

        Button(
            onClick = aoConfirmar,
            modifier = Modifier.weight(1f),
            enabled = quantidadeSelecionados > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = CoralVermelho,
                disabledContainerColor = Cinza300
            )
        ) {
            Text("Confirmar ($quantidadeSelecionados)")
        }
    }
}

@Composable
private fun AlbumCard(
    album: Album,
    estaSelecionado: Boolean,
    aoClicar: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (estaSelecionado) 3.dp else 1.dp,
                color = if (estaSelecionado) AzulTurquesa else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = aoClicar)
    ) {
        AsyncImage(
            model = album.capaUrl.ifBlank { null },
            contentDescription = album.titulo,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (estaSelecionado) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AzulTurquesa.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selecionado",
                    tint = BrancoTotal,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        InfoAlbum(album)
    }
}

@Composable
private fun BoxScope.InfoAlbum(album: Album) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomStart)
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.9f)
                    )
                )
            )
            .padding(8.dp)
    ) {
        Text(
            text = album.titulo,
            style = MaterialTheme.typography.bodySmall,
            color = BrancoTotal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = album.artista,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.9f
            ),
            color = Cinza700,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}