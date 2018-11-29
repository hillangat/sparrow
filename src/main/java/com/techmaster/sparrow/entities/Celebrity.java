package com.techmaster.sparrow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "CLBRTY")
public class Celebrity extends User {

    @OneToMany(mappedBy = "CLBRTY_ID")
    private List<Song> songs;

    @Column(name = "CLBRTY_TYP")
    private String celebrityType;

}
