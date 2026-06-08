package com.spotify.controller;

import com.spotify.entity.Artista;
import com.spotify.entity.Musica;
import com.spotify.service.ArtistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/artistas")
@RequiredArgsConstructor
public class ArtistaController {

    private final ArtistaService artistaService;

    @PostMapping
    public ResponseEntity<Artista> criar(@RequestBody Artista artista) {
        return ResponseEntity.ok(artistaService.criar(artista));
    }

    @GetMapping
    public ResponseEntity<List<Artista>> listar() {
        return ResponseEntity.ok(artistaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artista> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(artistaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artista> atualizar(@PathVariable Long id, @RequestBody Artista artista) {
        return ResponseEntity.ok(artistaService.atualizar(id, artista));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        artistaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sem-playlists")
    public ResponseEntity<List<Artista>> semPlaylists() {
        return ResponseEntity.ok(artistaService.listarArtistasSemMusicasEmPlaylists());
    }

    @GetMapping("/rank-popularidade")
    public ResponseEntity<List<Map<String, Object>>> rankPopularidade() {
        return ResponseEntity.ok(artistaService.rankPopularidade());
    }

    @GetMapping("/led-zeppelin-vs-queen")
    public ResponseEntity<List<Musica>> ledZeppelinVsQueen() {
        return ResponseEntity.ok(artistaService.musicasLedZeppelinMaioresQueMaxQueen());
    }
}
