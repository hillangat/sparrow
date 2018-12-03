package com.techmaster.sparrow.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "USR")
@Entity
public class User  extends AuditInfoBean implements Serializable {

    @Id()
    @Column(name = "usr_id", updatable = false, nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long userId;

    @Column(name = "usr_nam", nullable = false, unique = true)
    private String userName;

    @Column(name = "eml", nullable = false, unique = true)
    private String email;

    @Column(name = "frst_nam", nullable = false)
    private String  firstName;

    @Column(name = "lst_nam", nullable = false)
    private String lastName;

    @Column(name = "PRFL_PIC")
    private Blob profilePic;

    @Column(name = "NCK_NAM")
    private String nickName;

}
