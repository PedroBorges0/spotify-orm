package com.spotify.controller;

import com.spotify.entity.MusicaPlaylist;
import com.spotify.service.ArtistaService;
import com.spotify.service.MusicaService;
import com.spotify.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final MusicaService musicaService;
    private final ArtistaService artistaService;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> criar(
            @PathVariable Long usuarioId,
            @RequestParam String nome,
            @RequestParam(defaultValue = "true") Boolean publica) {
        return ResponseEntity.ok(playlistService.criarPlaylist(nome, usuarioId, publica));
    }

    @PostMapping("/{playlistId}/usuario/{usuarioId}/musicas/{musicaId}")
    public ResponseEntity<MusicaPlaylist> adicionarMusica(
            @PathVariable Long playlistId,
            @PathVariable Long usuarioId,
            @PathVariable Long musicaId) {
        return ResponseEntity.ok(playlistService.adicionarMusica(musicaId, playlistId, usuarioId));
    }

    @DeleteMapping("/{playlistId}/usuario/{usuarioId}/musicas/{musicaId}")
    public ResponseEntity<Void> removerMusica(
            @PathVariable Long playlistId,
            @PathVariable Long usuarioId,
            @PathVariable Long musicaId) {
        playlistService.removerMusica(musicaId, playlistId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<Map<String, Object>>> playlistsDeUsuario(@PathVariable String username) {
        return ResponseEntity.ok(playlistService.listarPlaylistsDeUsuario(username));
    }

    @GetMapping("/contagem-musicas")
    public ResponseEntity<List<Map<String, Object>>> contagemMusicas() {
        return ResponseEntity.ok(playlistService.contarMusicasPorPlaylist());
    }

    @GetMapping("/artistas-sem-playlist")
    public ResponseEntity<?> artistasSemPlaylist() {
        return ResponseEntity.ok(artistaService.listarArtistasSemMusicasEmPlaylists());
    }

    @GetMapping("/tempo-total")
    public ResponseEntity<List<Map<String, Object>>> tempoTotal() {
        return ResponseEntity.ok(playlistService.calcularTempoTotalPorPlaylist());
    }

    @GetMapping("/musicas-mais-curtas-media")
    public ResponseEntity<?> musicasMaisCurtasQueMedia() {
        return ResponseEntity.ok(musicaService.musicasMaisCurtasQueMediaDoArtista());
    }

    @GetMapping("/musicas-com-ordem")
    public ResponseEntity<List<Map<String, Object>>> musicasComOrdem(@RequestParam String nomePlaylist) {
        return ResponseEntity.ok(playlistService.musicasComOrdemNaPlaylist(nomePlaylist));
    }

    @GetMapping("/dono-por-musica")
    public ResponseEntity<List<String>> donoPorMusica(@RequestParam String tituloMusica) {
        return ResponseEntity.ok(playlistService.donoPlaylistPorMusica(tituloMusica));
    }

    @GetMapping("/rank-artistas")
    public ResponseEntity<List<Map<String, Object>>> rankArtistas() {
        return ResponseEntity.ok(artistaService.rankPopularidade());
    }

    @GetMapping("/led-zeppelin-vs-queen")
    public ResponseEntity<?> ledZeppelinVsQueen() {
        return ResponseEntity.ok(musicaService.musicasLedZeppelinMaioresQueMaxQueen());
    }

    @PostMapping("/transferir-musica")
    public ResponseEntity<String> transferirMusica(
            @RequestParam Long musicaId,
            @RequestParam Long playlistOrigemId,
            @RequestParam Long playlistDestinoId,
            @RequestParam Long usuarioId) {
        playlistService.transferirMusica(musicaId, playlistOrigemId, playlistDestinoId, usuarioId);
        return ResponseEntity.ok("Música transferida com sucesso (operação atômica)");
    }
}
