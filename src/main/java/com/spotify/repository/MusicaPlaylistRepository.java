package com.spotify.repository;

import com.spotify.entity.MusicaPlaylist;
import com.spotify.entity.pk.MusicaPlaylistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicaPlaylistRepository extends JpaRepository<MusicaPlaylist, MusicaPlaylistPK> {

    boolean existsById(MusicaPlaylistPK id);

    @Query("""
        SELECT mp FROM MusicaPlaylist mp
        JOIN FETCH mp.musica
        WHERE mp.playlist.id.playlistId = :playlistId
        AND mp.playlist.id.usuarioId = :usuarioId
        ORDER BY mp.ordemNaPlaylist
        """)
    List<MusicaPlaylist> findMusicasDaPlaylist(
        @Param("playlistId") Long playlistId,
        @Param("usuarioId") Long usuarioId
    );

    @Query("""
        SELECT m.titulo, mp.ordemNaPlaylist
        FROM MusicaPlaylist mp
        JOIN mp.musica m
        JOIN mp.playlist p
        WHERE p.nomePlaylist = :nomePlaylist
        ORDER BY mp.ordemNaPlaylist
        """)
    List<Object[]> findMusicasComOrdemNaPlaylist(@Param("nomePlaylist") String nomePlaylist);

    @Query("""
        SELECT mp FROM MusicaPlaylist mp
        WHERE mp.musica.id = :musicaId
        AND mp.playlist.id.playlistId = :playlistId
        AND mp.playlist.id.usuarioId = :usuarioId
        """)
    Optional<MusicaPlaylist> findByMusicaEPlaylist(
        @Param("musicaId") Long musicaId,
        @Param("playlistId") Long playlistId,
        @Param("usuarioId") Long usuarioId
    );

    @Query("""
        SELECT COUNT(mp) FROM MusicaPlaylist mp
        WHERE mp.playlist.id.playlistId = :playlistId
        AND mp.playlist.id.usuarioId = :usuarioId
        """)
    Long countMusicasNaPlaylist(
        @Param("playlistId") Long playlistId,
        @Param("usuarioId") Long usuarioId
    );
}
