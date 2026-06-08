package com.spotify.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "musica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"artista", "musicasPlaylists"})
public class Musica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "musica_id")
    private Long id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "duracao_segundos", nullable = false)
    private Integer duracaoSegundos;

    @Column(name = "album", length = 150)
    private String album;

    @Column(name = "ano_lancamento")
    private Integer anoLancamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id", nullable = false)
    private Artista artista;

    @OneToMany(mappedBy = "musica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicaPlaylist> musicasPlaylists = new ArrayList<>();
}
