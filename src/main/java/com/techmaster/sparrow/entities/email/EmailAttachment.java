package com.techmaster.sparrow.entities.email;

import com.techmaster.sparrow.entities.AuditInfoBean;
import com.techmaster.sparrow.enums.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Blob;

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

    @Column(name = "CNTNT", nullable = false)
    private Blob content;

    @Column(name = "ORGNL_NAM", nullable = false)
    private String originalName;

    @Column(name = "EXTNSN", nullable = false)
    private String extension;

    @Column(name = "BYT_SZ", nullable = false)
    private Double byteSize;

    @Column(name = "RVW_STS", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum reviewStatus;
}
