package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.entities.playlist.Song;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlaylistRepo extends CrudRepository<Playlist, Long> {

    @Query("SELECT p.songs from Playlist p WHERE p.playListId = ?1")
    List<Song> getPlaylistSongs(long playlistId);
}
