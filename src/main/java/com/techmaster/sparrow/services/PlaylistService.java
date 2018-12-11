package com.techmaster.sparrow.services;

import com.techmaster.sparrow.entities.SearchArg;
import com.techmaster.sparrow.entities.SearchResult;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.entities.playlist.SongOrder;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;

import java.util.List;

public interface PlaylistService {

    List<Playlist> getAllPlaylists();
    Playlist getPlaylistById( long playlistId );
    RuleResultBean saveOrEditPlaylist(Playlist playlist);
    RuleResultBean deletePlaylist(long playlistId);
    void likePlaylist( long userId, boolean like );
    void ratePlaylist( long playlistIid, long userId, int rating );
    Playlist contributeToPlaylist(List<SongOrder> songOrder);
    SearchResult searchPlaylist(SearchArg arg);
    SearchResult searchPlaylistSongs(Long playlistId, SearchArg arg);
}
