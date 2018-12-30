package com.techmaster.sparrow.entities.email;

import com.techmaster.sparrow.entities.misc.AuditInfoBean;
import com.techmaster.sparrow.enums.EmailReceiverType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * Email receiver every time there is an email sent.
 */

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "EML_RCVR")
public class EmailReceiver extends AuditInfoBean {

    @Id()
    @Column(name = "RCVR_ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eml_rcvr_gen")
    @SequenceGenerator(name="eml_rcvr_gen", sequenceName = "eml_rcvr_seq", allocationSize=100)
    private long receiverId;

    @Column(name = "EML", nullable = false)
    private String email;

    @Column(name = "CNTNT_ID", nullable = false)
    private long contentId;

    @Column(name = "RCVR_TYP", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailReceiverType receiverType;

    @Column(name = "FRST_NAM", nullable = false)
    private String firstName;

    @Column(name = "LST_NAM", nullable = false)
    private String lastName;

    @Column(name = "MDDL_NAM")
    private String middleName;

}
