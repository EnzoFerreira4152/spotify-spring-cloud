package com.spotify.playlist.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMusicDto {
    private Long playListId;
    private Long musicId;
}
