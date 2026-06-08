package com.spotify.repository;

import com.spotify.entity.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    Optional<Artista> findByNomeArtista(String nomeArtista);

    @Query("""
        SELECT a FROM Artista a
        WHERE a.id NOT IN (
            SELECT m.artista.id FROM MusicaPlaylist mp
            JOIN mp.musica m
        )
        """)
    List<Artista> findArtistasSemMusicasEmPlaylists();

    @Query("""
        SELECT a, COUNT(DISTINCT mp.playlist) as totalPlaylists
        FROM Artista a
        JOIN a.musicas m
        JOIN m.musicasPlaylists mp
        GROUP BY a
        ORDER BY totalPlaylists DESC
        """)
    List<Object[]> rankPopularidadeArtista();

    @Query("""
        SELECT m FROM Musica m
        JOIN m.artista a
        WHERE a.nomeArtista = :nomeArtista
        AND m.duracaoSegundos > (
            SELECT MAX(m2.duracaoSegundos) FROM Musica m2
            JOIN m2.artista a2
            WHERE a2.nomeArtista = :nomeComparacao
        )
        """)
    List<com.spotify.entity.Musica> findMusicasMaisLongasQueMaxDeOutroArtista(
        @Param("nomeArtista") String nomeArtista,
        @Param("nomeComparacao") String nomeComparacao
    );
}
