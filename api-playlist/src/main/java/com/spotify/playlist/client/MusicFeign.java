package com.spotify.playlist.client;

import com.spotify.playlist.exceptions.CircuitBreakerException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-music", url = "http://localhost:8081")
public interface MusicFeign {

    @GetMapping("/api/v1/musics/{id}")
    @CircuitBreaker(name = "music", fallbackMethod = "addMusicFallbackMethod")
    @Retry(name = "music")
    Music getById(@PathVariable Long id, Boolean throwError);

    @Getter
    @Setter
    class Music {
        private Long musicId;
        private String name;
    }

    private void addMusicFallbackMethod(CallNotPermittedException ex) throws CircuitBreakerException {
        throw new CircuitBreakerException("CircuitBreaker se activó: " + ex.getMessage());
    }
}
