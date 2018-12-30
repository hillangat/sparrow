package com.techmaster.sparrow.entities.playlist;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.misc.AuditInfoBean;
import com.techmaster.sparrow.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "SNG")
public class Song extends AuditInfoBean {

    @Id
    @Column(name = "SNG_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sng_gen")
    @SequenceGenerator(name="sng_gen", sequenceName = "sng_seq", allocationSize=100)
    private long songId;

    @Column(name = "ARTST", nullable = false)
    private String artist;

    @Column(name = "NAM", nullable = false)
    private String name;

    @Column(name = "GNR")
    private String genre;

    @Column(name = "RTNG", nullable = false)
    private int rating;

    @Column(name = "RTNG_CNT", nullable = false)
    private int ratingCount;

    @Column(name = "PICTR")
    private Blob picture;

    @Column(name = "PRDCR")
    private String producer;

    @Column(name = "RLS_DTE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    private LocalDateTime releaseDate;

    @Column(name = "ALBM")
    private String album;

    @Column(name = "LF_STS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status lifeStatus = Status.DRAFT;

    @Transient
    private int index;

}
