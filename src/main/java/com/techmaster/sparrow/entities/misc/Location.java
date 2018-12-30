package com.techmaster.sparrow.entities.misc;

import com.techmaster.sparrow.enums.LocationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.awt.event.HierarchyListener;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "LCTN")
@EntityListeners( { HierarchyListener.class })
public class Location extends AuditInfoBean {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lctn_gen")
    @SequenceGenerator(name="lctn_gen", sequenceName = "lctn_seq", allocationSize=100)
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

    @Column(name = "PRNT_ID")
    private long parentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "LCTN_TYP")
    private LocationType locationType;

    @Transient
    private long uiParentId;

    @Transient
    private long uiLocationId;

    @Transient
    private List<Location> subLocations = new ArrayList<>();
}
