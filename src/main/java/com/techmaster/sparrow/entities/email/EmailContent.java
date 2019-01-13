package com.techmaster.sparrow.entities.email;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.misc.AuditInfoBean;
import com.techmaster.sparrow.entities.misc.User;
import com.techmaster.sparrow.enums.EmailReasonType;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.util.SparrowUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contents sent on an email instance
 */

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "EML_CNTNT")
public class EmailContent extends AuditInfoBean {

    @Id()
    @Column(name = "CNTNT_ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eml_cntnt_gen")
    @SequenceGenerator(name="eml_cntnt_gen", sequenceName = "eml_cntnt_seq", allocationSize=100)
    private long contentId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "USR_ID", nullable = false)
    private User user;

    @Column(name = "RSN_TYP", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailReasonType reasonType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "SND_TIME")
    private LocalDateTime sendTime;

    @Column(name = "LF_STS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status lifeStatus = Status.DRAFT;

    @Column(name = "DLVRY_STS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status deliveryStatus = Status.CONCEPTUAL;

    @Column(name = "SBJCT", nullable = false)
    private String subject;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "TMPLT_ID", nullable = false)
    private EmailTemplate template;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="EML_CNTNT_EML_RCVR",
            joinColumns=@JoinColumn(name="CNTNT_ID", referencedColumnName="CNTNT_ID"),
            inverseJoinColumns=@JoinColumn(name="RCVR_ID", referencedColumnName="RCVR_ID"))
    private List<EmailReceiver> receivers = new ArrayList<>();

    @Transient
    private Map<String, String> params = new HashMap<>();
}
