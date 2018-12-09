package com.techmaster.sparrow.entities;

import com.techmaster.sparrow.enums.StorageType;
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
@Table(name = "MDA_OBJ")
public class MediaObj extends AuditInfoBean {

    @Id()
    @Column(name = "MDA_ID", updatable = false, nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long mediaId;

    @Column(name = "ORGNL_NAM")
    private String originalName;

    @Column(name = "URL")
    private String url;

    @Column(name = "STRG_TYP", nullable = false)
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    @Column(name = "BYT_SZ")
    private long byteSize;

    @Column(name = "CNTNT")
    private Blob content;

    @Column(name = "XTNSN", nullable = false)
    private String extension;

    @Column(name = "DSCRPTN")
    private String description;

    @Column(name = "OWNR")
    private String owner;

    @Column(name = "CNTNT_TYP", nullable = false)
    private String contentType;

}
