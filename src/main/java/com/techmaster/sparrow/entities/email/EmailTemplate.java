package com.techmaster.sparrow.entities.email;

import com.techmaster.sparrow.converters.BooleanToYNStringConverter;
import com.techmaster.sparrow.entities.misc.AuditInfoBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * Email template that determine corresponding email content
 */

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "EML_TMPLT")
public class EmailTemplate extends AuditInfoBean {

    @Id()
    @Column(name = "TMPLT_ID", updatable = false, nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long templateId;

    @Column(name = "TMPLT_NAM", nullable = false)
    private String templateName;

    @Column(name = "TMPLT_DESC")
    private String templateDescription;

    @Column(name = "CNTNT_BDY", nullable = false)
    private Blob contentBody;

    @Column(name = "DSCLMR")
    private Blob disclaimer;

    @Column(name = "GRTR")
    private Blob greeter;

    @Column(name = "JVSCRPT")
    private Blob javascript;

    @Column(name = "CSS")
    private Blob css;

    @Column(name = "HS_ATTCHMNT")
    @Convert(converter = BooleanToYNStringConverter.class)
    protected boolean hasAttachment;

    @Column(name = "FRM")
    private String from;

    @Column(name = "SBJCT")
    private String subject;

    @Column(name = "TO_LST")
    private Blob toList;

    @Column(name = "CC_LIST")
    private Blob ccList;

    @Column(name = "CNTNT_TYP")
    private String contentType;

    @Column(name = "EXCLSV")
    @Convert(converter = BooleanToYNStringConverter.class)
    private boolean exclusive;

    @Column(name = "IS_ALL_BBC")
    @Convert(converter = BooleanToYNStringConverter.class)
    private boolean isAllBcc;

    @Column(name = "CNTNT_KY")
    private String contentKey;

    @Column(name = "TMPLT")
    private Blob template;

    @ManyToMany
    @JoinTable(
            name="EML_TMPLT_ATTCHMNT",
            joinColumns=@JoinColumn(name="TMPLT_ID", referencedColumnName="TMPLT_ID"),
            inverseJoinColumns=@JoinColumn(name="ATTCHMNT_ID", referencedColumnName="ATTCHMNT_ID"))
    private List<EmailAttachment> attachments = new ArrayList<>();

}
