package com.techmaster.sparrow.entities.views;

import com.techmaster.sparrow.entities.playlist.Playlist;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PlaylistView extends Playlist {

    private double rating;
    private double ratingCount;

}
