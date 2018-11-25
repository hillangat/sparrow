package com.techmaster.sparrow.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Table(name = "USR")
public class User  extends AuditInfoBean implements Serializable {

    @Id @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long userId;

    private String userName;
    private String email;
    private String  firstName;
    private String lastName;

}
