package com.techmaster.sparrow.entities.email;

import com.techmaster.sparrow.converters.BooleanToYNStringConverter;
import com.techmaster.sparrow.entities.AuditInfoBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

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
    private String contentBody;

    @Column(name = "DSCLMR", nullable = false)
    private String disclaimer;

    @Column(name = "GRTR", nullable = false)
    private String greeter;

    @Column(name = "JVSCRPT", nullable = false)
    private String javascript;

    @Column(name = "CSS")
    private String css;

    @Column(name = "HS_ATTCHMNT")
    @Convert(converter = BooleanToYNStringConverter.class)
    protected boolean hasAttachment;

}
