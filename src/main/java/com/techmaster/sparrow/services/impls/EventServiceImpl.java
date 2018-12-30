package com.techmaster.sparrow.services.impls;

import com.techmaster.sparrow.entities.misc.Event;
import com.techmaster.sparrow.entities.misc.Rating;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.repositories.EventRepo;
import com.techmaster.sparrow.repositories.RatingRepo;
import com.techmaster.sparrow.repositories.SparrowJDBCExecutor;
import com.techmaster.sparrow.repositories.UserRepo;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;
import com.techmaster.sparrow.services.apis.EventService;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.validation.EventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired private EventValidator eventValidator;
    @Autowired private EventRepo eventRepo;
    @Autowired private SparrowJDBCExecutor sparrowJDBCExecutor;
    @Autowired private UserRepo userRepo;
    @Autowired private RatingRepo ratingRepo;

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public RuleResultBean createEvent(Event event, String userName) {
        RuleResultBean resultBean = eventValidator.validateCreate(event);
        if (resultBean.isSuccess()) {
            try {
                SparrowUtil.addAuditInfo(event, userName);
                eventRepo.save(event);
            } catch (Exception e) {
                SparrowUtil.logException(logger, e, "Error occurred while trying to create event");
                resultBean.setApplicationError(e);
            }
        }
        return resultBean;
    }

    @Override
    public RuleResultBean editEvent(Event event, String userName) {
        RuleResultBean resultBean = eventValidator.validateCreate(event);
        if (resultBean.isSuccess()) {
            try {
                eventRepo.save(event);
            } catch (Exception e) {
                SparrowUtil.logException(logger, e, "Exception occurred while trying to edit event: " + event.getEventName());
                resultBean.setApplicationError(e);
            }
        }
        return resultBean;
    }

    @Override
    public ResponseData searchEvents(GridDataQueryReq queryReq) {
        return null;
    }

    @Override
    public Event getEvent(long eventId) {
        Event event = SparrowUtil.getIfExist(eventRepo.findById(eventId));
        return event;
    }

    @Override
    public RuleResultBean deleteEvent(long eventId, String userName) {
        RuleResultBean resultBean = eventValidator.validateDelete(eventId);
        if (resultBean.isSuccess()) {
            String query = sparrowJDBCExecutor.getQueryForSqlId("logicallyDeleteEvent");
            sparrowJDBCExecutor.executeUpdate(query, sparrowJDBCExecutor.getList(userName, eventId));
        }
        return resultBean;
    }

    @Override
    public RuleResultBean rateEvent(long eventId, Rating rating) {
        RuleResultBean resultBean = new RuleResultBean();
        try {
            String userName = userRepo.getUserName(rating.getUserId());
            rating = SparrowUtil.addAuditInfo(rating, userName);
            ratingRepo.save(rating);
            Event event = SparrowUtil.getIfExist(eventRepo.findById(eventId));
            event.getRatings().add(rating);
            eventRepo.save(event);
        } catch (Exception e) {
            SparrowUtil.logException(logger, e, "Error occurred while trying to rate event");
            resultBean.setApplicationError(e);
        }
        return resultBean;
    }

    @Override
    public RuleResultBean addAttendant(long eventId, long userId) {
        RuleResultBean resultBean = new RuleResultBean();
        try {
            String query = sparrowJDBCExecutor.getQueryForSqlId("addEventAttendant");
            sparrowJDBCExecutor.executeUpdate(query, sparrowJDBCExecutor.getList(eventId, userId));
        } catch (Exception e) {
            SparrowUtil.logException(logger, e, "Application error occurred: addAttendant()");
            resultBean.setApplicationError(e);
        }
        return new RuleResultBean();
    }

    @Override
    public RuleResultBean addSongs(final long eventId, final List<Long> songIds) {

        RuleResultBean ruleResultBean = new RuleResultBean();
        String query = sparrowJDBCExecutor.getQueryForSqlId("");

        try {
            sparrowJDBCExecutor.getJDBCTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    preparedStatement.setLong(1, eventId);
                    preparedStatement.setLong(2, songIds.get(i));
                }

                @Override
                public int getBatchSize() {
                    return songIds.size();
                }
            });
        } catch (Exception e) {
            SparrowUtil.logException(logger, e, "Application error occurred: addSongs() ");
            ruleResultBean.setApplicationError(e);
        }

        return ruleResultBean;
    }

    @Override
    public RuleResultBean releaseEvent(long eventId, String userName) {
        RuleResultBean resultBean = eventValidator.validateEventPlanner(eventId, userName);
        if (resultBean.isSuccess()) {
            hideOrShowEvent(resultBean, eventId, "Y");
        }
        return new RuleResultBean();
    }

    @Override
    public RuleResultBean hideEvent(long eventId, String userName) {
        RuleResultBean resultBean = eventValidator.validateEventPlanner(eventId, userName);
        if (resultBean.isSuccess()) {
            hideOrShowEvent(resultBean, eventId, "N");
        }
        return new RuleResultBean();
    }

    private void hideOrShowEvent(RuleResultBean resultBean, long eventId, String type) {
        try {
            String query = sparrowJDBCExecutor.getQueryForSqlId("hideOrReleaseEventToPublic");
            sparrowJDBCExecutor.executeUpdate(query, sparrowJDBCExecutor.getList(eventId, type));
        } catch (Exception e) {
            SparrowUtil.logException(logger, e, "Application error occurred: addAttendant()");
            resultBean.setApplicationError(e);
        }
    }
}
