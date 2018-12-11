package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.playlist.Song;
import org.springframework.data.repository.CrudRepository;

public interface SongRepo extends CrudRepository<Song, Long> {

}
