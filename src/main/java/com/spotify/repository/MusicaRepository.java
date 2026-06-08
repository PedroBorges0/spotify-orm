package com.spotify.repository;

import com.spotify.entity.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicaRepository extends JpaRepository<Musica, Long> {

    // JOIN FETCH evita o problema N+1 ao carregar artista junto com a música
    @Query("""
        SELECT m FROM Musica m
        JOIN FETCH m.artista a
        WHERE m.id = :id
        """)
    Optional<Musica> findByIdComArtista(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT m FROM Musica m
        JOIN m.musicasPlaylists mp
        JOIN mp.playlist pl
        JOIN pl.usuario u
        JOIN m.artista a
        WHERE u.username = :username
        AND a.nomeArtista = :nomeArtista
        """)
    List<Musica> findMusicasEmPlaylistsDeUsuarioPorArtista(
        @Param("username") String username,
        @Param("nomeArtista") String nomeArtista
    );

    @Query("""
        SELECT m FROM Musica m
        WHERE m.duracaoSegundos < (
            SELECT AVG(m2.duracaoSegundos) FROM Musica m2
            WHERE m2.artista = m.artista
        )
        ORDER BY m.artista.nomeArtista, m.duracaoSegundos
        """)
    List<Musica> findMusicasMaisCurtasQueMediaDoArtista();

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
    List<Musica> findMusicasMaisLongasQueMaxDeOutroArtista(
        @Param("nomeArtista") String nomeArtista,
        @Param("nomeComparacao") String nomeComparacao
    );
}
