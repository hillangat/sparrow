package com.techmaster.sparrow.entities.misc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.converters.BooleanToYNStringConverter;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.enums.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "EVNT")
public class Event extends AuditInfoBean{

    @Id
    @Column(name = "EVNT_ID", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long eventId;

    @Column(name = "EVNT_NAM", nullable = false)
    private String eventName;

    @Column(name = "EVNT_TYP", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "STRT_TIME", nullable = false)
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "END_TIME", nullable = false)
    private LocalDateTime endTime;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "PLYLST_ID")
    private Playlist playlist;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "LCTN_ID")
    private Location location;

    @Column(name = "BSNSS_NAM", nullable = false)
    private String businessName;

    @Column(name = "SHW", nullable = false)
    @Convert(converter = BooleanToYNStringConverter.class)
    private boolean show = false;

    @Column(name = "DLTD", nullable = false)
    @Convert(converter = BooleanToYNStringConverter.class)
    private boolean deleted;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DJ_USR_ID")
    private User dj;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "EVNT_PLNNR_USR_ID")
    private User eventPlanner;

    @Column(name = "PRMS_OWNR_EML", nullable = false)
    private String premiseOwnerEmail;

    @Column(name = "PRMS_OWNR_FULL_NAM", nullable = false)
    private String premiseOwnerFullName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="EVNT_RTNGS",
            joinColumns=@JoinColumn(name="EVNT_ID", referencedColumnName="EVNT_ID"),
            inverseJoinColumns=@JoinColumn(name="RTNG_ID", referencedColumnName="RTNG_ID"))
    private List<Rating> ratings = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name="EVNT_ATTNDNTS",
            joinColumns=@JoinColumn(name="EVNT_ID", referencedColumnName="EVNT_ID"),
            inverseJoinColumns=@JoinColumn(name="USR_ID", referencedColumnName="USR_ID"))
    private List<User> attendants = new ArrayList<>();


}
