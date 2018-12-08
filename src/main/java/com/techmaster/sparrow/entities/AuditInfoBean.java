package com.techmaster.sparrow.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Embeddable
@MappedSuperclass
public abstract class AuditInfoBean implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "cret_dt", nullable = false)
    protected LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "lst_updt", nullable = false)
    protected LocalDateTime lastUpdate;

    @Column(name = "cretd_by", nullable = false)
    protected String createdBy;

    @Column(name = "updtd_by", nullable = false)
    protected String updatedBy;
}
