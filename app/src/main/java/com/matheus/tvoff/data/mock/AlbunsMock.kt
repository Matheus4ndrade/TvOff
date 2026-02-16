package com.matheus.tvoff.data.mock

import com.matheus.tvoff.data.modelo.Album

/**
 * Lista mockada de álbuns para testes
 * TODO: Substituir por API real (Spotify/Last.fm/Deezer)
 */
object AlbunsMock {
    
    val albuns = listOf(
        // Rock/Alternative
        Album(
            id = "1",
            titulo = "In Rainbows",
            artista = "Radiohead",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/1/14/Inrainbowscover.png",
            anoLancamento = 2007
        ),
        Album(
            id = "2",
            titulo = "OK Computer",
            artista = "Radiohead",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/b/ba/Radioheadokcomputer.png",
            anoLancamento = 1997
        ),
        Album(
            id = "3",
            titulo = "Kid A",
            artista = "Radiohead",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/0/02/Radioheadkida.png",
            anoLancamento = 2000
        ),
        Album(
            id = "4",
            titulo = "The Dark Side of the Moon",
            artista = "Pink Floyd",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/3/3b/Dark_Side_of_the_Moon.png",
            anoLancamento = 1973
        ),
        Album(
            id = "5",
            titulo = "Wish You Were Here",
            artista = "Pink Floyd",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/a/a4/Pink_Floyd%2C_Wish_You_Were_Here_%281975%29.png",
            anoLancamento = 1975
        ),
        
        // Hip Hop
        Album(
            id = "6",
            titulo = "To Pimp a Butterfly",
            artista = "Kendrick Lamar",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/f/f6/Kendrick_Lamar_-_To_Pimp_a_Butterfly.png",
            anoLancamento = 2015
        ),
        Album(
            id = "7",
            titulo = "good kid, m.A.A.d city",
            artista = "Kendrick Lamar",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/c/c4/Kendrick_Lamar_good_kid_m_A_A_d_city.jpg",
            anoLancamento = 2012
        ),
        Album(
            id = "8",
            titulo = "My Beautiful Dark Twisted Fantasy",
            artista = "Kanye West",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/9/96/My_Beautiful_Dark_Twisted_Fantasy.jpg",
            anoLancamento = 2010
        ),
        
        // MPB/Brasil
        Album(
            id = "9",
            titulo = "Acabou Chorare",
            artista = "Novos Baianos",
            capaUrl = "https://upload.wikimedia.org/wikipedia/pt/1/18/Acabou_Chorare.jpg",
            anoLancamento = 1972
        ),
        Album(
            id = "10",
            titulo = "Clube da Esquina",
            artista = "Milton Nascimento & Lô Borges",
            capaUrl = "https://upload.wikimedia.org/wikipedia/pt/8/86/Clube_da_Esquina.jpg",
            anoLancamento = 1972
        ),
        Album(
            id = "11",
            titulo = "Chega de Saudade",
            artista = "João Gilberto",
            capaUrl = "https://upload.wikimedia.org/wikipedia/pt/f/f4/Chega_de_Saudade.jpg",
            anoLancamento = 1959
        ),
        
        // Pop/Electronic
        Album(
            id = "12",
            titulo = "Random Access Memories",
            artista = "Daft Punk",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/a/a7/Random_Access_Memories.jpg",
            anoLancamento = 2013
        ),
        Album(
            id = "13",
            titulo = "Discovery",
            artista = "Daft Punk",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/2/27/Daft_Punk_-_Discovery.png",
            anoLancamento = 2001
        ),
        Album(
            id = "14",
            titulo = "1989",
            artista = "Taylor Swift",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/f/f6/Taylor_Swift_-_1989.png",
            anoLancamento = 2014
        ),
        
        // Jazz
        Album(
            id = "15",
            titulo = "Kind of Blue",
            artista = "Miles Davis",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/9/9c/MilesDavisKindofBlue.jpg",
            anoLancamento = 1959
        ),
        Album(
            id = "16",
            titulo = "A Love Supreme",
            artista = "John Coltrane",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/7/7d/A_Love_Supreme.jpg",
            anoLancamento = 1965
        ),
        
        // Indie/Alternative
        Album(
            id = "17",
            titulo = "Is This It",
            artista = "The Strokes",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/0/09/IsThisIt.png",
            anoLancamento = 2001
        ),
        Album(
            id = "18",
            titulo = "Funeral",
            artista = "Arcade Fire",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/6/68/Arcade_Fire_-_Funeral.jpg",
            anoLancamento = 2004
        ),
        Album(
            id = "19",
            titulo = "In the Aeroplane Over the Sea",
            artista = "Neutral Milk Hotel",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/d/df/In_the_Aeroplane_Over_the_Sea_cover.jpg",
            anoLancamento = 1998
        ),
        Album(
            id = "20",
            titulo = "The Queen Is Dead",
            artista = "The Smiths",
            capaUrl = "https://upload.wikimedia.org/wikipedia/en/9/92/The_Smiths-The_Queen_Is_Dead.png",
            anoLancamento = 1986
        )
    )
    
    /**
     * Busca álbuns por título ou artista
     */
    fun buscar(query: String): List<Album> {
        if (query.isBlank()) return albuns
        
        val queryLower = query.lowercase()
        return albuns.filter { album ->
            album.titulo.lowercase().contains(queryLower) ||
            album.artista.lowercase().contains(queryLower)
        }
    }
}
