package com.spotify.service;

import com.spotify.entity.Artista;
import com.spotify.entity.Musica;
import com.spotify.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArtistaService {

    private final ArtistaRepository artistaRepository;

    @Transactional
    public Artista criar(Artista artista) {
        return artistaRepository.save(artista);
    }

    @Transactional(readOnly = true)
    public Artista buscarPorId(Long id) {
        return artistaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Artista não encontrado: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Artista> listarTodos() {
        return artistaRepository.findAll();
    }

    @Transactional
    public Artista atualizar(Long id, Artista dadosNovos) {
        Artista artista = buscarPorId(id);
        artista.setNomeArtista(dadosNovos.getNomeArtista());
        artista.setPaisOrigem(dadosNovos.getPaisOrigem());
        artista.setAnoFormacao(dadosNovos.getAnoFormacao());
        // save() faz UPDATE porque a entidade já tem ID
        return artistaRepository.save(artista);
    }

    @Transactional
    public void deletar(Long id) {
        Artista artista = buscarPorId(id);
        artistaRepository.delete(artista);
    }

    @Transactional(readOnly = true)
    public List<Artista> listarArtistasSemMusicasEmPlaylists() {
        return artistaRepository.findArtistasSemMusicasEmPlaylists();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> rankPopularidade() {
        List<Object[]> resultados = artistaRepository.rankPopularidadeArtista();
        return resultados.stream().map(row -> {
            Artista artista = (Artista) row[0];
            Long totalPlaylists = (Long) row[1];
            return Map.<String, Object>of(
                "artistaId", artista.getId(),
                "nomeArtista", artista.getNomeArtista(),
                "pais", artista.getPaisOrigem(),
                "totalPlaylists", totalPlaylists
            );
        }).toList();
    }

    @Transactional(readOnly = true)
    public List<Musica> musicasLedZeppelinMaioresQueMaxQueen() {
        return artistaRepository.findMusicasMaisLongasQueMaxDeOutroArtista(
            "Led Zeppelin", "Queen"
        );
    }
}
