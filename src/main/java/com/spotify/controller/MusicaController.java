package com.spotify.controller;

import com.spotify.entity.Musica;
import com.spotify.service.MusicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/musicas")
@RequiredArgsConstructor
public class MusicaController {

    private final MusicaService musicaService;

    @PostMapping("/{artistaId}")
    public ResponseEntity<Musica> criar(@PathVariable Long artistaId, @RequestBody Musica musica) {
        return ResponseEntity.ok(musicaService.criar(musica, artistaId));
    }

    @GetMapping
    public ResponseEntity<List<Musica>> listar() {
        return ResponseEntity.ok(musicaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Musica> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(musicaService.buscarPorId(id));
    }

    @GetMapping("/{id}/com-artista")
    public ResponseEntity<Musica> buscarComArtista(@PathVariable Long id) {
        return ResponseEntity.ok(musicaService.buscarComArtista(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Musica> atualizar(@PathVariable Long id, @RequestBody Musica musica) {
        return ResponseEntity.ok(musicaService.atualizar(id, musica));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        musicaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-usuario-artista")
    public ResponseEntity<List<Musica>> porUsuarioEArtista(
            @RequestParam String username,
            @RequestParam String nomeArtista) {
        return ResponseEntity.ok(musicaService.musicasEmPlaylistsDeUsuarioPorArtista(username, nomeArtista));
    }

    @GetMapping("/mais-curtas-que-media")
    public ResponseEntity<List<Musica>> maisCurtasQueMedia() {
        return ResponseEntity.ok(musicaService.musicasMaisCurtasQueMediaDoArtista());
    }

    @GetMapping("/led-zeppelin-vs-queen")
    public ResponseEntity<List<Musica>> ledZeppelinVsQueen() {
        return ResponseEntity.ok(musicaService.musicasLedZeppelinMaioresQueMaxQueen());
    }
}
