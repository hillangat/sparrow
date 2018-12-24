package com.techmaster.sparrow.entities;

import com.techmaster.sparrow.entities.misc.AuditInfoBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "USR_ROLES")
@Entity
public class UserRole extends AuditInfoBean {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "RL_ID")
    private long roleId;

    @Column(name = "RL_NAM", unique = true)
    private String roleName;

    @Column(name = "RL_DESC")
    private String roleDesc;

}
