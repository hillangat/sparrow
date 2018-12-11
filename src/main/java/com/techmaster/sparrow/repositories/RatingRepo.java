package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.Rating;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepo extends CrudRepository<Rating, Long> {
}
