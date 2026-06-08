package com.spotify.repository;

import com.spotify.entity.Playlist;
import com.spotify.entity.pk.PlaylistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, PlaylistPK> {

    List<Playlist> findByUsuarioId(Long usuarioId);

    Optional<Playlist> findByNomePlaylist(String nomePlaylist);

    @Query("""
        SELECT p.nomePlaylist, p.dataCriacao
        FROM Playlist p
        JOIN p.usuario u
        WHERE u.username = :username
        ORDER BY p.dataCriacao DESC
        """)
    List<Object[]> findPlaylistsDeUsuario(@Param("username") String username);

    @Query("""
        SELECT p.nomePlaylist, COUNT(mp) as totalMusicas
        FROM Playlist p
        LEFT JOIN p.musicasPlaylists mp
        GROUP BY p.id, p.nomePlaylist
        ORDER BY totalMusicas DESC
        """)
    List<Object[]> countMusicasPorPlaylist();

    @Query("""
        SELECT p.nomePlaylist, u.username, COALESCE(SUM(m.duracaoSegundos), 0)
        FROM Playlist p
        JOIN p.usuario u
        LEFT JOIN p.musicasPlaylists mp
        LEFT JOIN mp.musica m
        GROUP BY p.id, p.nomePlaylist, u.username
        ORDER BY p.nomePlaylist
        """)
    List<Object[]> calcularTempoTotalPorPlaylist();

    @Query("""
        SELECT u.username
        FROM Playlist p
        JOIN p.usuario u
        JOIN p.musicasPlaylists mp
        JOIN mp.musica m
        WHERE m.titulo = :tituloMusica
        """)
    List<String> findDonoPlaylistPorMusica(@Param("tituloMusica") String tituloMusica);
}
