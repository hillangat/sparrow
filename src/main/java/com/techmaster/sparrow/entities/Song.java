package com.techmaster.sparrow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "SNG")
public class Song extends AuditInfoBean {

    @Id
    @Column(name = "SNG_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long songId;

    @Column(name = "CLBRTY_ID", nullable = false)
    private String celebrityId;

    @Column(name = "NAM", nullable = false)
    private String name;

    @Column(name = "GNR")
    private String genre;

    @Column(name = "RTNG", nullable = false)
    private int rating;

    @Column(name = "RTNG_CNT", nullable = false)
    private int ratingCount;

    @Column(name = "PICTR", nullable = false)
    private Blob picture;

}
