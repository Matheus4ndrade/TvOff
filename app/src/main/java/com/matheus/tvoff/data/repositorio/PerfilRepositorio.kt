package com.matheus.tvoff.data.repositorio

import android.util.Log
import com.matheus.tvoff.data.modelo.Perfil
import com.matheus.tvoff.data.supabase.SupabaseConfig
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PerfilRepositorio {

    private val supabase = SupabaseConfig.cliente
    private val TAG = "PerfilRepositorio"

    suspend fun nicknameExiste(nickname: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val resultado = supabase
                .from("perfis")
                .select(columns = Columns.list("nickname")) {
                    filter { eq("nickname", nickname.lowercase()) }
                }
                .decodeList<Map<String, String>>()

            resultado.isNotEmpty()
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao verificar nickname", e)
            false
        }
    }

    suspend fun buscarPerfil(nickname: String): Perfil? = withContext(Dispatchers.IO) {
        try {
            val resultado = supabase
                .from("perfis")
                .select {
                    filter { eq("nickname", nickname.lowercase()) }
                }
                .decodeList<Perfil>()

            resultado.firstOrNull()
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar perfil", e)
            null
        }
    }

    suspend fun salvarPerfil(perfil: Perfil): Boolean = withContext(Dispatchers.IO) {
        try {
            supabase.from("perfis").insert(perfil)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao salvar perfil", e)
            false
        }
    }

    suspend fun atualizarPerfil(perfil: Perfil): Boolean = withContext(Dispatchers.IO) {
        try {
            supabase
                .from("perfis")
                .update(perfil) {
                    filter { eq("nickname", perfil.nickname.lowercase()) }
                }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao atualizar perfil", e)
            false
        }
    }

    suspend fun deletarPerfil(nickname: String): Boolean = withContext(Dispatchers.IO) {
        try {
            supabase
                .from("perfis")
                .delete {
                    filter { eq("nickname", nickname.lowercase()) }
                }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao deletar perfil", e)
            false
        }
    }
}