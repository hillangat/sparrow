package com.techmaster.sparrow.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.converters.BooleanToYNStringConverter;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "USR")
@Entity
public class User  extends AuditInfoBean {

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

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "ACTV")
    private boolean active;

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

}
