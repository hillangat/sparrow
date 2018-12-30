package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.misc.Event;
import com.techmaster.sparrow.entities.misc.Rating;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.entities.misc.SelectOption;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;
import com.techmaster.sparrow.services.apis.EventService;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController extends BaseController {

    @Autowired private EventService eventService;

    @PostMapping("event")
    public ResponseEntity<ResponseData> createOrEditEvent(@RequestBody Event event) {

        String userName = getUserName();

        RuleResultBean resultBean = event.getEventId() > 0
                ? eventService.editEvent(event, userName) : eventService.createEvent(event, userName);

        return getResponse(false, event, resultBean);
    }

    @DeleteMapping("event/{eventId}")
    public ResponseEntity<ResponseData> deleteEvent(@PathVariable("eventId") Long eventId) {
        RuleResultBean resultBean = eventService.deleteEvent(eventId, getUserName());
        return getResponse(false, null, resultBean);
    }

    @GetMapping("event/{eventId}")
    public ResponseEntity<ResponseData> getEventById(@PathVariable("eventId") Long eventId) {
        Event event = eventService.getEvent(eventId);
        return getResponse(false, event, null);
    }

    @PostMapping("event/search")
    public ResponseEntity<ResponseData> searchEvents(@RequestBody GridDataQueryReq queryReq) {
        ResponseData responseData = eventService.searchEvents(queryReq);
        return getResponse(false, responseData, null);
    }

    @PostMapping("event/{eventId}/rating")
    public ResponseEntity<ResponseData> rateEvent(@PathVariable("eventId") Long eventId, @RequestBody Rating rating) {
        RuleResultBean resultBean = eventService.rateEvent(eventId, rating);
        return getResponse(false, null, resultBean);
    }

    @PostMapping("event/{eventId}/attendants")
    public ResponseEntity<ResponseData> registerEventAttendant(@PathVariable("eventId") Long eventId, @RequestBody SelectOption selectOption) {
        Long userId = SparrowUtil.getLongFromObject(selectOption.getValue());
        RuleResultBean resultBean = eventService.addAttendant(eventId, userId);
        return getResponse(false, null, resultBean);
    }

    @PostMapping("event/{eventId}/playlist/songs")
    public ResponseEntity<ResponseData> addSongs(@PathVariable("eventId") Long eventId, @RequestBody List<Long> songIds) {
        RuleResultBean resultBean = eventService.addSongs(eventId, songIds);
        return getResponse(false, null, resultBean);
    }

    @PostMapping("event/{eventId}/release")
    public ResponseEntity<ResponseData> releaseEvent(@PathVariable("eventId") Long eventId) {
        RuleResultBean resultBean = eventService.releaseEvent(eventId, getUserName());
        return getResponse(false, null, resultBean);
    }

    @PostMapping("event/{eventId}/hide")
    public ResponseEntity<ResponseData> hideEvent(@PathVariable("eventId") Long eventId) {
        RuleResultBean resultBean = eventService.releaseEvent(eventId, getUserName());
        return getResponse(false, null, resultBean);
    }

}
