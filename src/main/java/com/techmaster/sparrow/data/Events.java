package com.techmaster.sparrow.data;

import com.techmaster.sparrow.entities.misc.Event;
import com.techmaster.sparrow.entities.misc.Location;
import com.techmaster.sparrow.entities.misc.Rating;
import com.techmaster.sparrow.entities.misc.User;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.enums.EventType;
import com.techmaster.sparrow.util.SparrowUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Events {

    public static List<Event> loadEvents(List<User> attendants, List<Playlist> playlists, Location location, List<Rating> ratings) {

        List<Event> events = new ArrayList<>();

        playlists.forEach(playlist -> {
            Event event = SparrowUtil.addAuditInfo(new Event(), "admin");
            event.setAttendants(attendants);
            event.setBusinessName("Tally Ho");
            event.setDeleted(false);
            event.setDj(attendants.get(0));
            event.setEndTime(LocalDateTime.now().plusYears(100));
            event.setStartTime(LocalDateTime.now().plusDays(2));
            event.setEventName("Saturday night at Tally Ho");
            event.setEventPlanner(attendants.get(0));
            event.setEventType(EventType.NIGHT_CLUB);
            event.setPlaylist(playlist);
            event.setLocation(location);
            event.setPremiseOwnerEmail("hillangat@gmail.com");
            event.setPremiseOwnerFullName("Hillary Langat");
            event.setRatings(ratings);
            event.setShow(true);
            events.add(event);
        });

        return events;
    }

}
