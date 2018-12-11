package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.playlist.Playlist;
import org.springframework.data.repository.CrudRepository;

public interface PlaylistRepo extends CrudRepository<Playlist, Long> {

}
