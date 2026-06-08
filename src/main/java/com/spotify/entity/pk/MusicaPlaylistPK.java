package com.spotify.entity.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicaPlaylistPK implements Serializable {

    @Column(name = "musica_id")
    private Long musicaId;

    @Column(name = "playlist_id")
    private Long playlistId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MusicaPlaylistPK that)) return false;
        return Objects.equals(musicaId, that.musicaId) &&
               Objects.equals(playlistId, that.playlistId) &&
               Objects.equals(usuarioId, that.usuarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(musicaId, playlistId, usuarioId);
    }
}
