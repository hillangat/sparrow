package com.techmaster.sparrow.entities.configs;


import com.techmaster.sparrow.entities.AuditInfoBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "APPCTN_CNFGS")
@Entity
@IdClass(ConfigIdClass.class)
public class ApplicationConfigs extends AuditInfoBean {

    @Id
    @Column(name = "cde", nullable = false)
    private String code;

    @Id
    @Column(name = "cntxt", nullable = false)
    private String context;

    @Column(name = "txt", nullable = false)
    private String text;

    @Column(name = "desc", nullable = false)
    private String description;

}
