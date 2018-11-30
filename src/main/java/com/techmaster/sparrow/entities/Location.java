package com.techmaster.sparrow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "PRNT_ID")
    private long parentId;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn(name = "PRNT_ID")
    private List<Location> subLocations = new ArrayList<>();
}
