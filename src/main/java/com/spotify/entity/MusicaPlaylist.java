package com.spotify.entity;

import com.spotify.entity.pk.MusicaPlaylistPK;
import jakarta.persistence.*;
import lombok.*;

// @MapsId composto não funciona bem no Hibernate 6, por isso a PK é montada manualmente
@Entity
@Table(name = "musica_playlist")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"musica", "playlist"})
public class MusicaPlaylist {

    @EmbeddedId
    private MusicaPlaylistPK id;

    @Column(name = "ordem_na_playlist")
    private Integer ordemNaPlaylist;

    // insertable/updatable = false porque as colunas já estão no @EmbeddedId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musica_id", insertable = false, updatable = false)
    private Musica musica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id", insertable = false, updatable = false),
        @JoinColumn(name = "usuario_id",  referencedColumnName = "usuario_id",  insertable = false, updatable = false)
    })
    private Playlist playlist;

    public MusicaPlaylist(Musica musica, Playlist playlist, Integer ordem) {
        this.id = new MusicaPlaylistPK(
            musica.getId(),
            playlist.getId().getPlaylistId(),
            playlist.getId().getUsuarioId()
        );
        this.musica = musica;
        this.playlist = playlist;
        this.ordemNaPlaylist = ordem;
    }
}
