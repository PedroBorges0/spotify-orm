package com.spotify.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
    name = "artista",
    uniqueConstraints = @UniqueConstraint(columnNames = "nome_artista")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "musicas")  // evita loop infinito no toString
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artista_id")
    private Long id;

    @Column(name = "nome_artista", nullable = false, unique = true, length = 150)
    private String nomeArtista;

    @Column(name = "pais_origem", nullable = false, length = 100)
    private String paisOrigem;

    @Column(name = "ano_formacao")
    private Integer anoFormacao;

    @JsonIgnore
    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Musica> musicas = new ArrayList<>();
}
