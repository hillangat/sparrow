package com.techmaster.sparrow.services.apis;

import com.techmaster.sparrow.entities.playlist.Song;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;

import java.util.List;

public interface SongService {

    RuleResultBean createOrUpdateSong(Song song, String userName );
    RuleResultBean createOrUpdateSongs(List<Song> song, String userName );
    Song getSongById( long songId );
    List<Song> searchSongs(GridDataQueryReq queryReq);
    List<Song> getAllSongs();
    RuleResultBean deleteSong( long songId, String userName);
    RuleResultBean changeStatus(long songId, String toStatus);
    int getSongPlaylistCount( long songId);


}
