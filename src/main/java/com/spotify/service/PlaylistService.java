package com.spotify.service;

import com.spotify.entity.*;
import com.spotify.entity.pk.MusicaPlaylistPK;
import com.spotify.entity.pk.PlaylistPK;
import com.spotify.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UsuarioRepository usuarioRepository;
    private final MusicaRepository musicaRepository;
    private final MusicaPlaylistRepository musicaPlaylistRepository;

    @Transactional
    public Playlist criarPlaylist(String nomePlaylist, Long usuarioId, Boolean publica) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: id=" + usuarioId));

        Long playlistId = System.currentTimeMillis();

        PlaylistPK pk = new PlaylistPK(playlistId, usuarioId);

        Playlist playlist = new Playlist();
        playlist.setId(pk);
        playlist.setNomePlaylist(nomePlaylist);
        playlist.setUsuario(usuario);
        playlist.setPublica(publica != null ? publica : true);

        return playlistRepository.save(playlist);
    }

    @Transactional
    public MusicaPlaylist adicionarMusica(Long musicaId, Long playlistId, Long usuarioId) {
        Musica musica = musicaRepository.findById(musicaId)
            .orElseThrow(() -> new RuntimeException("Música não encontrada: id=" + musicaId));

        PlaylistPK pk = new PlaylistPK(playlistId, usuarioId);
        Playlist playlist = playlistRepository.findById(pk)
            .orElseThrow(() -> new RuntimeException("Playlist não encontrada"));

        MusicaPlaylistPK mpPk = new MusicaPlaylistPK(musicaId, playlistId, usuarioId);
        if (musicaPlaylistRepository.existsById(mpPk)) {
            throw new RuntimeException("Música já está nessa playlist");
        }

        Long totalAtual = musicaPlaylistRepository.countMusicasNaPlaylist(playlistId, usuarioId);
        int proximaOrdem = totalAtual.intValue() + 1;

        MusicaPlaylist mp = new MusicaPlaylist(musica, playlist, proximaOrdem);
        return musicaPlaylistRepository.save(mp);
    }

    @Transactional
    public void removerMusica(Long musicaId, Long playlistId, Long usuarioId) {
        MusicaPlaylist mp = musicaPlaylistRepository
            .findByMusicaEPlaylist(musicaId, playlistId, usuarioId)
            .orElseThrow(() -> new RuntimeException("Música não encontrada nessa playlist"));

        musicaPlaylistRepository.delete(mp);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarPlaylistsDeUsuario(String username) {
        List<Object[]> rows = playlistRepository.findPlaylistsDeUsuario(username);
        return rows.stream().map(row -> Map.of(
            "nomePlaylist", row[0],
            "dataCriacao", row[1]
        )).toList();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> contarMusicasPorPlaylist() {
        List<Object[]> rows = playlistRepository.countMusicasPorPlaylist();
        return rows.stream().map(row -> Map.of(
            "nomePlaylist", row[0],
            "totalMusicas", row[1]
        )).toList();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> calcularTempoTotalPorPlaylist() {
        List<Object[]> rows = playlistRepository.calcularTempoTotalPorPlaylist();
        return rows.stream().map(row -> Map.of(
            "nomePlaylist", row[0],
            "dono", row[1],
            "tempoTotalSegundos", row[2]
        )).toList();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> musicasComOrdemNaPlaylist(String nomePlaylist) {
        List<Object[]> rows = musicaPlaylistRepository.findMusicasComOrdemNaPlaylist(nomePlaylist);
        return rows.stream().map(row -> Map.of(
            "titulo", row[0],
            "ordemNaPlaylist", row[1]
        )).toList();
    }

    @Transactional(readOnly = true)
    public List<String> donoPlaylistPorMusica(String tituloMusica) {
        return playlistRepository.findDonoPlaylistPorMusica(tituloMusica);
    }

    @Transactional(rollbackFor = Exception.class)
    public void transferirMusica(
            Long musicaId,
            Long playlistOrigemId,
            Long playlistDestinoId,
            Long usuarioId) {

        PlaylistPK pkOrigem = new PlaylistPK(playlistOrigemId, usuarioId);
        PlaylistPK pkDestino = new PlaylistPK(playlistDestinoId, usuarioId);

        Playlist origem = playlistRepository.findById(pkOrigem)
            .orElseThrow(() -> new RuntimeException("Playlist de origem não encontrada"));
        Playlist destino = playlistRepository.findById(pkDestino)
            .orElseThrow(() -> new RuntimeException("Playlist de destino não encontrada"));

        if (!origem.getUsuario().getId().equals(destino.getUsuario().getId())) {
            throw new RuntimeException("As playlists devem pertencer ao mesmo usuário");
        }

        MusicaPlaylist mpOrigem = musicaPlaylistRepository
            .findByMusicaEPlaylist(musicaId, playlistOrigemId, usuarioId)
            .orElseThrow(() -> new RuntimeException("Música não encontrada na playlist de origem"));

        musicaPlaylistRepository.delete(mpOrigem);
        musicaPlaylistRepository.flush(); // precisa forçar o DELETE antes do INSERT pra não violar PK

        Musica musica = musicaRepository.findById(musicaId)
            .orElseThrow(() -> new RuntimeException("Música não encontrada"));

        Long totalDestino = musicaPlaylistRepository.countMusicasNaPlaylist(playlistDestinoId, usuarioId);
        MusicaPlaylist mpDestino = new MusicaPlaylist(musica, destino, totalDestino.intValue() + 1);
        musicaPlaylistRepository.save(mpDestino);
    }
}
