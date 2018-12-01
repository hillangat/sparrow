package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.Location;
import org.springframework.data.repository.CrudRepository;

public interface DataLoaderConfigRepository extends CrudRepository<Location, Long> {

}
