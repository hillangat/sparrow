package com.techmaster.sparrow.services;

import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.entities.misc.SearchArg;
import com.techmaster.sparrow.entities.misc.SearchResult;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.entities.playlist.SongOrder;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;

import java.util.List;

public interface PlaylistService {

    List<Playlist> getAllPlaylists();
    Playlist getPlaylistById( long playlistId );
    RuleResultBean saveOrEditPlaylist(Playlist playlist);
    RuleResultBean deletePlaylist(long playlistId);
    void ratePlaylist( long playlistIid, long userId, int rating );
    Playlist contributeSongOrder(long playlistId, List<SongOrder> songOrder);
    SearchResult searchPlaylist(SearchArg arg);
    SearchResult searchPlaylistSongs(Long playlistId, SearchArg arg);
    ResponseData paginate(GridDataQueryReq queryReq);
}
