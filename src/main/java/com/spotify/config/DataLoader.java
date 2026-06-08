package com.spotify.config;

import com.spotify.entity.*;
import com.spotify.entity.pk.PlaylistPK;
import com.spotify.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ArtistaRepository artistaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MusicaRepository musicaRepository;
    private final PlaylistRepository playlistRepository;
    private final MusicaPlaylistRepository musicaPlaylistRepository;

    // em produção usaria sequence do banco
    private final AtomicLong playlistIdGen = new AtomicLong(1L);

    @Override
    @Transactional
    public void run(String... args) {
        log.info("=====> Iniciando carga de dados de exemplo...");

        Artista queen    = salvarArtista("Pantera",        "Estados Unidos", 1981);
        Artista acdc     = salvarArtista("AC/DC",        "Austrália",   1973);
        Artista led      = salvarArtista("Led Zeppelin", "Reino Unido", 1968);
        Artista beatles  = salvarArtista("The Beatles",  "Reino Unido", 1960);
        Artista semMusica = salvarArtista("Artista Sem Música", "Brasil", 2020);

        Usuario pablo     = salvarUsuario("Pablo",     "pablo@email.com",     "senha123");
        Usuario alexandre = salvarUsuario("Alexandre", "alex@email.com",      "senha123");
        Usuario josue     = salvarUsuario("Josue",     "josue@email.com",     "senha123");

        Musica bohemian   = salvarMusica("Bohemian Rhapsody",  354, "A Night at the Opera",    1975, queen);
        Musica wewill     = salvarMusica("We Will Rock You",   122, "News of the World",       1977, queen);
        Musica dontStop   = salvarMusica("Don't Stop Me Now",  209, "Jazz",                    1979, queen);
        Musica radioGaGa  = salvarMusica("Radio Ga Ga",        343, "The Works",               1984, queen);

        Musica highway    = salvarMusica("Highway to Hell",    208, "Highway to Hell",         1979, acdc);
        Musica thunders   = salvarMusica("Thunderstruck",      292, "The Razors Edge",         1990, acdc);
        Musica backBlack  = salvarMusica("Back in Black",      255, "Back in Black",           1980, acdc);

        Musica stairway   = salvarMusica("Stairway to Heaven", 482, "Led Zeppelin IV",         1971, led);
        Musica wholeLotta = salvarMusica("Whole Lotta Love",   334, "Led Zeppelin II",         1969, led);
        Musica kashmir    = salvarMusica("Kashmir",            508, "Physical Graffiti",       1975, led);

        Musica letItBe    = salvarMusica("Let It Be",          243, "Let It Be",               1970, beatles);
        Musica yesterday  = salvarMusica("Yesterday",          125, "Help!",                   1965, beatles);

        // Playlists do Pablo
        Playlist rockPablo = salvarPlaylist("Rock do Pablo", pablo, true);
        adicionarMusicaPlaylist(bohemian,  rockPablo, 1);
        adicionarMusicaPlaylist(wewill,    rockPablo, 2);
        adicionarMusicaPlaylist(highway,   rockPablo, 3);
        adicionarMusicaPlaylist(thunders,  rockPablo, 4);
        adicionarMusicaPlaylist(stairway,  rockPablo, 5);

        Playlist classicsPablo = salvarPlaylist("Clássicos do Rock", pablo, true);
        adicionarMusicaPlaylist(backBlack, classicsPablo, 1);
        adicionarMusicaPlaylist(kashmir,   classicsPablo, 2);
        adicionarMusicaPlaylist(letItBe,   classicsPablo, 3);

        // Playlists do Alexandre
        Playlist classicsAlex = salvarPlaylist("Clássicos do Alexandre", alexandre, true);
        adicionarMusicaPlaylist(bohemian,   classicsAlex, 1);
        adicionarMusicaPlaylist(dontStop,   classicsAlex, 2);
        adicionarMusicaPlaylist(radioGaGa,  classicsAlex, 3);
        adicionarMusicaPlaylist(wholeLotta, classicsAlex, 4);

        // Playlists do Josue
        Playlist queenPlaylist = salvarPlaylist("Só Queen", josue, false);
        adicionarMusicaPlaylist(bohemian,  queenPlaylist, 1);
        adicionarMusicaPlaylist(wewill,    queenPlaylist, 2);
        adicionarMusicaPlaylist(dontStop,  queenPlaylist, 3);
        adicionarMusicaPlaylist(radioGaGa, queenPlaylist, 4);

        Playlist misturada = salvarPlaylist("Misturada do Josue", josue, true);
        adicionarMusicaPlaylist(highway,   misturada, 1);
        adicionarMusicaPlaylist(yesterday, misturada, 2);
        adicionarMusicaPlaylist(stairway,  misturada, 3);

        log.info("=====> Dados carregados com sucesso!");
        log.info("Acesse http://localhost:8080/artistas para testar.");
    }

    private Artista salvarArtista(String nome, String pais, int ano) {
        Artista a = new Artista();
        a.setNomeArtista(nome);
        a.setPaisOrigem(pais);
        a.setAnoFormacao(ano);
        return artistaRepository.save(a);
    }

    private Usuario salvarUsuario(String username, String email, String senha) {
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setEmail(email);
        u.setSenha(senha);
        return usuarioRepository.save(u);
    }

    private Musica salvarMusica(String titulo, int duracao, String album, int ano, Artista artista) {
        Musica m = new Musica();
        m.setTitulo(titulo);
        m.setDuracaoSegundos(duracao);
        m.setAlbum(album);
        m.setAnoLancamento(ano);
        m.setArtista(artista);
        return musicaRepository.save(m);
    }

    private Playlist salvarPlaylist(String nome, Usuario usuario, boolean publica) {
        Long plId = playlistIdGen.getAndIncrement();
        PlaylistPK pk = new PlaylistPK(plId, usuario.getId());
        Playlist p = new Playlist();
        p.setId(pk);
        p.setNomePlaylist(nome);
        p.setUsuario(usuario);
        p.setPublica(publica);
        return playlistRepository.save(p);
    }

    private void adicionarMusicaPlaylist(Musica musica, Playlist playlist, int ordem) {
        MusicaPlaylist mp = new MusicaPlaylist(musica, playlist, ordem);
        musicaPlaylistRepository.save(mp);
    }
}
