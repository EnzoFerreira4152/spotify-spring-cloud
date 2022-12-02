package com.spotify.playlist.service;

import com.spotify.playlist.client.MusicFeign;
import com.spotify.playlist.exceptions.CircuitBreakerException;
import com.spotify.playlist.model.PlayListMusic;
import com.spotify.playlist.model.Playlist;
import com.spotify.playlist.repository.PlaylistRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    private final MusicFeign musicFeign;

    public PlaylistService(PlaylistRepository playlistRepository, MusicFeign musicFeign) {
        this.playlistRepository = playlistRepository;
        this.musicFeign = musicFeign;
    }

    public void save(Playlist playlist) {
        playlistRepository.save(playlist);
    }

    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    public Playlist getById(Long id) {
        return playlistRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        playlistRepository.deleteById(id);
    }

    public void update(Playlist playlist) {
        if (playlistRepository.existsById(playlist.getPlayListId())) {
            playlistRepository.save(playlist);
        }
    }

    @CircuitBreaker(name = "music", fallbackMethod = "addMusicFallbackMethod")
    @Retry(name = "music")
    public void addMusic(Long idPlayList, Long idMusic) throws Exception {
        System.out.println("Ejecutando addMusic...");
        Optional<Playlist> playList = playlistRepository.findById(idPlayList);
        var result = musicFeign.getById(idMusic, true);
        if (playList.isPresent()) {
            playList.get().getMusics().add(new PlayListMusic(null, playList.get(),result.getMusicId(),result.getName()));
            playlistRepository.save(playList.get());
        }else{
            throw new Exception("Playlist not found");
        }
    }

    private void addMusicFallbackMethod(CallNotPermittedException ex) throws CircuitBreakerException {
        throw new CircuitBreakerException("CircuitBreaker se activó: " + ex.getMessage());
    }

}
