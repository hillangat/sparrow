package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.entities.misc.Event;
import com.techmaster.sparrow.repositories.EventRepo;
import com.techmaster.sparrow.repositories.UserRepo;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventValidator extends AbstractValidator {

    @Autowired private EventRepo eventRepo;
    @Autowired private UserRepo userRepo;

    public RuleResultBean validateCreate( Event event ) {
        RuleResultBean resultBean = new RuleResultBean();

        validateEmpty(resultBean, event.getEventName(), "eventName", "Event name is required");
        validateEmpty(resultBean, event.getBusinessName(), "businessName", "Business name is required");
        validateEmpty(resultBean, event.getStartTime(), "startTime", "Start time is required");
        validateEmpty(resultBean, event.getEndTime(), "endTime", "End time is required");
        validateEmpty(resultBean, event.getPremiseOwnerEmail(), "premiseOwnerEmail", "Premise owner email is required");
        validateEmpty(resultBean, event.getPremiseOwnerFullName(), "premiseOwnerFullName", "Premise owner full name is required");

        if (event.getStartTime().isAfter(event.getEndTime())) {
            resultBean.setError("startTime", "Start time cannot be after end time");
        }

        return resultBean;
    }

    public RuleResultBean validateDelete( long eventId ) {
        Event event = SparrowUtil.getIfExist(eventRepo.getStartAndEntTime(eventId));
        RuleResultBean resultBean = new RuleResultBean();
        if (LocalDateTime.now().isBefore(event.getEndTime()) &&
                event.getStartTime().isAfter(LocalDateTime.now()) ) {

            resultBean.setError("deleted", "Event is already started and cannot be deleted!");
        }
        return resultBean;
    }

    public RuleResultBean validateEventPlanner(long eventId, String userName) {
        RuleResultBean ruleResultBean = new RuleResultBean();
        long userId = userRepo.getUserId(userName);
        long userIdDB = SparrowUtil.getIfExist(eventRepo.getEventPlannerId(eventId));
        if (userIdDB != userId) {
            ruleResultBean.setError("eventPlanner", "Only event planner can release an event");
        }
        return ruleResultBean;
    }

}
