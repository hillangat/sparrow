package com.techmaster.sparrow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.print.attribute.standard.MediaSize;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "LCTN")
public class Location extends AuditInfoBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LCTN_ID", nullable = false)
    private long locationId;

    @Column(name = "NAM")
    private String name;

    @Column(name = "CDE")
    private String code;

    @Column(name = "LGTDE")
    private double longitude;

    @Column(name = "LTTDE")
    private double latitude;

    @Column(name = "SUB_LCTNS")
    private Set<Location> subLocations;
}
