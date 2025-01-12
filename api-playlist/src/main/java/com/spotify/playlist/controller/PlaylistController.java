package com.spotify.playlist.controller;

import com.spotify.playlist.dto.AddMusicDto;
import com.spotify.playlist.model.Playlist;
import com.spotify.playlist.service.PlaylistService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/playlists")
public class PlaylistController {

    private final PlaylistService playListService;

    public PlaylistController(PlaylistService playListService) {
        this.playListService = playListService;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<Playlist>> getAll() {
        return ResponseEntity.ok(playListService.getAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Playlist> getById(@PathVariable Long id) {
        return ResponseEntity.ok(playListService.getById(id));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Long> create(@RequestBody Playlist playlist) {
        playListService.save(playlist);
        return ResponseEntity.ok(playlist.getPlayListId());
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity update(@RequestBody Playlist playlist) {
        playListService.update(playlist);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/addMusic")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity addMusic(
            @RequestBody AddMusicDto addMusicDto,
            @RequestParam(name = "error", defaultValue = "false") Boolean throwError)
            throws Exception {
        try {
            playListService.addMusic(addMusicDto.getPlayListId(), addMusicDto.getMusicId(), throwError);
       } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Long id) {
        playListService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
