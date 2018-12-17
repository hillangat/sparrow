package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.misc.Event;
import org.hibernate.annotations.NamedQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EventRepo extends CrudRepository<Event, Long> {

    @Query("SELECT a.startTime, a.endTime, a.eventId FROM Event a WHERE a.eventId = ?0")
    Optional<Event> getStartAndEntTime(long eventId);

    @Query("SELECT a.eventPlanner.userId FROM Event a WHERE a.eventId = ?0")
    Optional<Long> getEventPlannerId( long eventId );

}
