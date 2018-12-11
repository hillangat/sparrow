package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import org.springframework.stereotype.Component;

@Component
public class PlaylistValidator extends AbstractValidator {

    public RuleResultBean validateCreate(Playlist playlist) {
        RuleResultBean resultBean = new RuleResultBean();
        // playlist name
        validateSpecialChar( resultBean, playlist.getPlaylistName(),
                "playlistName", "Playlist cannot have special characters." );
        validateEmpty(resultBean, playlist.getPlaylistName(),
                "playlistName", "Playlist name is required");


        return resultBean;
    }

}
