package com.spotify.playlist.restassured;

import com.spotify.playlist.dto.AddMusicDto;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class TestAddSongToPlaylist {

    @Test
    void Add_a_song_to_playlist(){
        AddMusicDto musicDto = new AddMusicDto();

        musicDto.setMusicId(1L);
        musicDto.setPlayListId(2L);

        given()
                .contentType(ContentType.JSON)
                .body(musicDto)
                .when()
                .post("http://localhost:8080/api/v1/playlists/addMusic")
                .then()
                .statusCode(201);
    }
}
