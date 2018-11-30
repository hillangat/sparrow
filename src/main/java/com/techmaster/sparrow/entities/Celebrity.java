package com.techmaster.sparrow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "CLBRTY")
public class Celebrity extends User {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "CLBRTY_ID")
    private List<Song> songs;

    @Column(name = "CLBRTY_TYP")
    private String celebrityType;

}
