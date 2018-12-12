package com.techmaster.sparrow.entities.misc;


import com.techmaster.sparrow.enums.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Embeddable
@MappedSuperclass
public class Gathering extends AuditInfoBean{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "EVNT_TYP", nullable = false)
    private EventType eventType;

    @Column(name = "DESC", nullable = false)
    private String desc;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LCTN_ID")
    private Location location;

}
