package com.techmaster.sparrow.entities.email;

import com.techmaster.sparrow.converters.BooleanToYNStringConverter;
import com.techmaster.sparrow.entities.misc.AuditInfoBean;
import com.techmaster.sparrow.entities.misc.MediaObj;
import com.techmaster.sparrow.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * Email attachment content
 */

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "EML_ATTCHMNT")
public class EmailAttachment extends AuditInfoBean {

    @Id()
    @Column(name = "ATTCHMNT_ID", updatable = false, nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long attachmentId;

    @Column(name = "DSCRPTN", nullable = false)
    private String description;

    @Column(name = "WDTH")
    private int width;

    @Column(name = "HGHT")
    private int height;

    @Column(name = "EMBDD")
    @Convert(converter = BooleanToYNStringConverter.class)
    private boolean embedded;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "MDA_ID")
    private MediaObj mediaObj;

    @Column(name = "KY", nullable = false)
    private String key;

    @Column(name = "RVW_STS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status reviewStatus = Status.DRAFT;

    @Column(name = "UNIT", nullable = false)
    private String unit;

    @Column(name = "MDA_REF", nullable = false)
    private String mediaRef;



}
