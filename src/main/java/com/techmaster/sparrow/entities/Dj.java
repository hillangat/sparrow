package com.techmaster.sparrow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "DJ")
public class Dj extends User {

    @Column(name = "RTNG")
    private int rating = 1;

    @Column(name = "DJ_TYP")
    private String djType;
}
