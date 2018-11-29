package com.techmaster.sparrow.entities;

import java.time.LocalDateTime;

public class EventAttendant extends AuditInfoBean{

    private long userId;
    private long eventId;
    private LocalDateTime arrival;
    private LocalDateTime departure;


}
