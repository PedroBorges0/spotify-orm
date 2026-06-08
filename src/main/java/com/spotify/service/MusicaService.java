package com.spotify.service;

import com.spotify.entity.Artista;
import com.spotify.entity.Musica;
import com.spotify.repository.ArtistaRepository;
import com.spotify.repository.MusicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicaService {

    private final MusicaRepository musicaRepository;
    private final ArtistaRepository artistaRepository;

    @Transactional
    public Musica criar(Musica musica, Long artistaId) {
        if (musica.getDuracaoSegundos() <= 0) {
            throw new IllegalArgumentException("Duração deve ser maior que 0");
        }
        Artista artista = artistaRepository.findById(artistaId)
            .orElseThrow(() -> new RuntimeException("Artista não encontrado: id=" + artistaId));
        musica.setArtista(artista);
        return musicaRepository.save(musica);
    }

    @Transactional(readOnly = true)
    public Musica buscarPorId(Long id) {
        return musicaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Música não encontrada: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Musica> listarTodas() {
        return musicaRepository.findAll();
    }

    @Transactional
    public Musica atualizar(Long id, Musica dadosNovos) {
        Musica musica = buscarPorId(id);
        musica.setTitulo(dadosNovos.getTitulo());
        musica.setDuracaoSegundos(dadosNovos.getDuracaoSegundos());
        musica.setAlbum(dadosNovos.getAlbum());
        musica.setAnoLancamento(dadosNovos.getAnoLancamento());
        return musicaRepository.save(musica);
    }

    @Transactional
    public void deletar(Long id) {
        Musica musica = buscarPorId(id);
        musicaRepository.delete(musica);
    }

    @Transactional(readOnly = true)
    public Musica buscarComArtista(Long id) {
        return musicaRepository.findByIdComArtista(id)
            .orElseThrow(() -> new RuntimeException("Música não encontrada: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Musica> musicasEmPlaylistsDeUsuarioPorArtista(String username, String nomeArtista) {
        return musicaRepository.findMusicasEmPlaylistsDeUsuarioPorArtista(username, nomeArtista);
    }

    @Transactional(readOnly = true)
    public List<Musica> musicasMaisCurtasQueMediaDoArtista() {
        return musicaRepository.findMusicasMaisCurtasQueMediaDoArtista();
    }

    @Transactional(readOnly = true)
    public List<Musica> musicasLedZeppelinMaioresQueMaxQueen() {
        return musicaRepository.findMusicasMaisLongasQueMaxDeOutroArtista(
            "Led Zeppelin", "Queen"
        );
    }
}
