package com.techmaster.sparrow.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "USR")
public class User  extends AuditInfoBean implements Serializable {

    @Id()
    @Column(name = "usr_id", updatable = false, nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long userId;

    @Column(name = "usr_nam", nullable = false)
    private String userName;

    @Column(name = "eml", nullable = false)
    private String email;

    @Column(name = "frst_nam", nullable = false)
    private String  firstName;

    @Column(name = "lst_nam", nullable = false)
    private String lastName;

}
