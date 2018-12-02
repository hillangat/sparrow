package com.techmaster.sparrow.entities;

import com.techmaster.sparrow.enums.FileTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "DTLD_CNFG")
public class DataLoaderConfig extends AuditInfoBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LD_ID", nullable = false)
    private long loadId;

    @Column(name = "NAM", nullable = false)
    private String name;

    @Column(name = "FL_LCTN", nullable = false)
    private String fileLocation;

    @Column(name = "FL_TYP", nullable = false)
    private FileTypeEnum fileType;

    @Column(name = "LDED")
    private String loaded;

    @Column(name = "LD_DTE")
    private LocalDateTime loadDate;

    @Column(name = "TBL", nullable = false)
    private String table;

    @Column(name = "ID_CLMN", nullable = false)
    private String idColumn;

    @Column(name = "FLE")
    private Blob file;

    @Column(name = "FLE_EXTNSN", nullable = false)
    private String fileExtension;

    @Column(name = "EXTRCTR", nullable = false)
    private String extractor;

}
