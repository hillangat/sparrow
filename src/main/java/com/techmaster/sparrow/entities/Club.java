package com.techmaster.sparrow.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CLB")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Club extends AuditInfoBean {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "NAM", nullable = false)
    private String name;

    @Column(name = "CNTRY", nullable = false)
    private String country;

    @Column(name = "STTE", nullable = true)
    private String state;

    @Column(name = "CTY", nullable = false)
    private String city;


}
