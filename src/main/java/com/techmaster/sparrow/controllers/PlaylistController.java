package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.entities.misc.SearchResult;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.entities.playlist.Song;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;
import com.techmaster.sparrow.services.apis.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlaylistController extends BaseController {

    @Autowired PlaylistService playlistService;

    @PostMapping(value = "playlist")
    public ResponseEntity<ResponseData> createEdit(@RequestBody Playlist playlist) {
        RuleResultBean resultBean = playlistService.saveOrEditPlaylist(playlist);
        return getResponse(false, playlist, resultBean);
    }

    @PostMapping(value = "playlist/pagination")
    public ResponseEntity<ResponseData> createEdit(@RequestBody GridDataQueryReq queryReq) {
        ResponseData result = playlistService.paginate(queryReq);
        return getResponse(false, result, null);
    }

    @PostMapping(value = "playlist/search")
    public ResponseEntity<ResponseData> searchPlaylist(@RequestBody GridDataQueryReq queryReq) {
        SearchResult searchResult = playlistService.searchPlaylist(queryReq);
        return getResponse(true, searchResult, null);
    }

    @GetMapping(value = "playlist/{playlistId}/songs")
    public ResponseEntity<ResponseData> searchPlaylistSongs(@PathVariable("playlistId") Long playlistId) {
        List<Song> songs = playlistService.getOrderedPlaylistSongs(playlistId);
        return getResponse(true, songs, null);
    }


    @GetMapping(value = "playlist")
    public ResponseEntity<ResponseData> getPlaylists() {
        List<Playlist> playlist = playlistService.getAllPlaylists();
        return getResponse(true, playlist, null);
    }

    @GetMapping(value = "playlist/{playlistId}")
    public ResponseEntity<ResponseData> getPlaylist(@PathVariable(value = "playlistId") Long playlistId) {
        Playlist playlist = playlistService.getPlaylistById(playlistId);
        return getResponse(true, playlist, null);
    }

    @DeleteMapping(value = "playlist/{playlistId}")
    public ResponseEntity<ResponseData> deletePlaylist(@PathVariable(value = "playlistId") Long playlistId) {
        RuleResultBean resultBean = playlistService.deletePlaylist(playlistId, getUserName());
        return getResponse(true, null, resultBean);
    }

}
