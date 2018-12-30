package com.techmaster.sparrow.entities.misc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.converters.BooleanToYNStringConverter;
import com.techmaster.sparrow.enums.UserDisableReasonType;
import lombok.*;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "USR")
@Entity
public class User  extends AuditInfoBean {

    @Id()
    @Column(name = "USR_ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_gen")
    @SequenceGenerator(name="usr_gen", sequenceName = "usr_seq", allocationSize=100)
    private long userId;

    @Column(name = "USR_NAM", nullable = false, unique = true)
    private String userName;

    @Column(name = "PSSWRD", nullable = false)
    private String password;

    @Column(name = "EML", nullable = false, unique = true)
    private String email;

    @Column(name = "FRST_NAM", nullable = false)
    private String  firstName;

    @Column(name = "MDDL_NAM")
    private String  middleName;

    @Column(name = "LST_NAM", nullable = false)
    private String lastName;

    @Column(name = "PRFL_PIC")
    private Blob profilePic;

    @Column(name = "NCK_NAM")
    private String nickName;

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "ACTV")
    private boolean active;

    @Column(name = "DSBL_RSN")
    @Enumerated(EnumType.STRING)
    private UserDisableReasonType disableReasonType;

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "LCKD")
    private boolean locked;

    @Column(name = "LCKD_RSN")
    private String lockedReason;

    @Column(name = "LCKD_BY")
    private String lockedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "LCKD_ON")
    private LocalDateTime lockedOn;

    @Column(name = "EML_CNFRMD", nullable = false)
    @Convert(converter = BooleanToYNStringConverter.class)
    private boolean emailConfirmed;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(
            name="USR_USR_ROLES",
            joinColumns=@JoinColumn(name="USR_ID", referencedColumnName="USR_ID"),
            inverseJoinColumns=@JoinColumn(name="RL_ID", referencedColumnName="RL_ID"))
    private Set<UserRole> userRoles = new HashSet<>();

}
