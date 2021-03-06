package com.techmaster.sparrow.entities.email;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.AuditInfoBean;
import com.techmaster.sparrow.enums.EmailReasonType;
import com.techmaster.sparrow.enums.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long contentId;

    @Column(name = "RSN_TYP", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailReasonType reasonType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "SND_TIME", nullable = false)
    private LocalDateTime sendTime;

    @Column(name = "LF_STS", nullable = false)
    private StatusEnum lifeStatus = StatusEnum.DRAFT;

    @Column(name = "DLVRY_STS", nullable = false)
    private StatusEnum deliveryStatus = StatusEnum.CONCEPTUAL;

    @Transient
    private EmailTemplate template;

    @Transient
    private List<EmailReceiver> receivers;
}
