package com.spotify.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spotify.entity.pk.PlaylistPK;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"usuario", "musicasPlaylists"})
public class Playlist {

    @EmbeddedId
    private PlaylistPK id;

    @Column(name = "nome_playlist", nullable = false, length = 150)
    private String nomePlaylist;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "publica")
    private Boolean publica = true;

    // @MapsId vincula o usuarioId da PK composta à FK do relacionamento
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @JsonIgnore
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicaPlaylist> musicasPlaylists = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }
}
