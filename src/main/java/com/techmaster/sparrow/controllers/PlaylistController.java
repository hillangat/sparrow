package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.ResponseData;
import com.techmaster.sparrow.entities.SearchArg;
import com.techmaster.sparrow.entities.SearchResult;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.services.PlaylistService;
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

    // @PostMapping(value = "playlist/search")
    public ResponseEntity<ResponseData> searchPlaylist(@RequestBody SearchArg searchArg) {
        SearchResult searchResult = playlistService.searchPlaylist(searchArg);
        return getResponse(true, searchResult, null);
    }

    @PostMapping(value = "playlist/{playlistId}/songs")
    public ResponseEntity<ResponseData> searchPlaylistSongs(
            @PathVariable("playlistId") Long playlistId, @RequestBody SearchArg searchArg) {

        SearchResult searchResult = playlistService.searchPlaylistSongs(playlistId, searchArg);
        return getResponse(true, searchResult, null);
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

}
