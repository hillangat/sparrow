package com.techmaster.sparrow.entities.misc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "USR_LGN")
@Entity
public class UserLogin extends AuditInfoBean {

    @Id()
    @Column(name = "USR_LGN_ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_lgn_gen")
    @SequenceGenerator(name="usr_lgn_gen", sequenceName = "usr_lgn_seq", allocationSize=100)
    private long userLoginId;

    @Column(name = "STS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "USR_NAM", nullable = false)
    private String userName;

    @Column(name = "PSSWRD")
    private String password;

    @Column(name = "IP_ADDRSS", nullable = false)
    private String ipAddress;

}
