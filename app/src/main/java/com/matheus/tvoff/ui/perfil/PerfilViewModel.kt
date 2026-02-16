package com.matheus.tvoff.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.tvoff.data.modelo.Album
import com.matheus.tvoff.data.modelo.Perfil
import com.matheus.tvoff.data.repositorio.PerfilRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class EstadoPerfil {
    object Carregando : EstadoPerfil()
    data class Sucesso(val perfil: Perfil) : EstadoPerfil()
    object NaoExiste : EstadoPerfil()
    data class Erro(val mensagem: String) : EstadoPerfil()
}

class PerfilViewModel : ViewModel() {

    private val repositorio = PerfilRepositorio()
    private val _estado = MutableStateFlow<EstadoPerfil>(EstadoPerfil.Carregando)
    val estado: StateFlow<EstadoPerfil> = _estado.asStateFlow()

    private var perfilAtual: Perfil? = null

    fun carregarPerfil(nickname: String) {
        viewModelScope.launch {
            _estado.value = EstadoPerfil.Carregando

            try {
                val perfil = repositorio.buscarPerfil(nickname)

                if (perfil != null) {
                    perfilAtual = perfil
                    _estado.value = EstadoPerfil.Sucesso(perfil)
                } else {
                    _estado.value = EstadoPerfil.NaoExiste
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _estado.value = EstadoPerfil.Erro(
                    "Erro ao carregar perfil: ${e.message}"
                )
            }
        }
    }

    suspend fun criarPerfil(perfil: Perfil, senha: String): Boolean {
        return try {
            _estado.value = EstadoPerfil.Carregando

            val existe = repositorio.nicknameExiste(perfil.nickname)
            if (existe) {
                _estado.value = EstadoPerfil.Erro("Nickname já está em uso")
                return false
            }

            val perfilComSenha = perfil.copy(senhaHash = senha)
            val sucesso = repositorio.salvarPerfil(perfilComSenha)

            if (sucesso) {
                perfilAtual = perfilComSenha
                _estado.value = EstadoPerfil.Sucesso(perfilComSenha)
                true
            } else {
                _estado.value = EstadoPerfil.Erro("Erro ao criar perfil no banco")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _estado.value = EstadoPerfil.Erro("Erro: ${e.message}")
            false
        }
    }

    suspend fun atualizarAlbensFavoritos(albuns: List<Album>) {
        val perfil = perfilAtual ?: run {
            _estado.value = EstadoPerfil.Erro("Perfil não carregado")
            return
        }

        try {
            _estado.value = EstadoPerfil.Carregando

            val perfilAtualizado = perfil.copy(
                albensFavoritos = albuns.take(5)
            )

            val sucesso = repositorio.atualizarPerfil(perfilAtualizado)

            if (sucesso) {
                perfilAtual = perfilAtualizado
                _estado.value = EstadoPerfil.Sucesso(perfilAtualizado)
            } else {
                _estado.value = EstadoPerfil.Erro("Erro ao salvar álbuns")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _estado.value = EstadoPerfil.Erro("Erro: ${e.message}")
        }
    }

    suspend fun adicionarAlbumOuvido(
        album: Album,
        avaliacao: com.matheus.tvoff.data.modelo.TipoAvaliacao
    ) {
        val perfil = perfilAtual ?: return

        try {
            val novaAvaliacao = com.matheus.tvoff.data.modelo.AvaliacaoAlbum(
                album = album,
                avaliacao = avaliacao
            )

            val listaAtualizada = perfil.albensOuvidos
                .filter { it.album.id != album.id }
                .toMutableList()
            listaAtualizada.add(0, novaAvaliacao)

            val perfilAtualizado = perfil.copy(
                albensOuvidos = listaAtualizada
            )

            val sucesso = repositorio.atualizarPerfil(perfilAtualizado)

            if (sucesso) {
                perfilAtual = perfilAtualizado
                _estado.value = EstadoPerfil.Sucesso(perfilAtualizado)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _estado.value = EstadoPerfil.Erro("Erro ao adicionar álbum: ${e.message}")
        }
    }
}