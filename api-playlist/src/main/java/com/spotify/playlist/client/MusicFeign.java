package com.spotify.playlist.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-music", url = "http://localhost:8081")
public interface MusicFeign {

    @GetMapping("/api/v1/musics/{id}")
    Music getById(@PathVariable Long id, @RequestParam(defaultValue = "false") Boolean throwError);

    @Getter
    @Setter
    class Music {
        private Long musicId;
        private String name;
    }
}
