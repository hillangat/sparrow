package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.playlist.SongOrder;
import org.springframework.data.repository.CrudRepository;

public interface SongOrderRepo extends CrudRepository<SongOrder, Long> {
}
