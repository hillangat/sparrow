package com.techmaster.sparrow.entities.misc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.playlist.Playlist;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "EVNT")
public class Event extends AuditInfoBean{

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "STRT_TIME", nullable = false)
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "END_TIME", nullable = false)
    private LocalDateTime endTime;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "USR_ID")
    private Playlist playlist;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usr_id")
    private User dj;



}
