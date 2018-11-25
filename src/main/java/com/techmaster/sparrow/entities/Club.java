package com.techmaster.sparrow.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CLB")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Club extends AuditInfoBean {

    @Id @GeneratedValue
    private long id;
    private String name;
    private String country;
    private String state;
    private String city;


}
