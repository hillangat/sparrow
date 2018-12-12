package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.misc.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {


    @Query("SELECT MAX(a.locationId) FROM Location a")
    Long getMaxId();
}
