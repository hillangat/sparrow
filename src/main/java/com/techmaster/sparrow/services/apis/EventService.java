package com.techmaster.sparrow.services.apis;

import com.techmaster.sparrow.entities.misc.Event;
import com.techmaster.sparrow.entities.misc.Rating;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;

import java.util.List;

public interface EventService {

    RuleResultBean createEvent(Event event, String userName );
    RuleResultBean editEvent(Event even, String userName);
    ResponseData searchEvents(GridDataQueryReq queryReq);
    Event getEvent( long eventId );
    RuleResultBean deleteEvent( long eventId, String userName );
    RuleResultBean rateEvent(long eventId, Rating rating );
    RuleResultBean addAttendant( long eventId, long userId );
    RuleResultBean addSongs( long eventId, List<Long> songId );
    RuleResultBean releaseEvent( long eventId, String userName);
    RuleResultBean hideEvent( long eventId, String userName);

}
