package com.techmaster.sparrow.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Embeddable
@MappedSuperclass
public abstract class AuditInfoBean {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "cret_dt", nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    @Column(name = "lst_updt", nullable = false)
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @Column(name = "cretd_by", nullable = false)
    private String createdBy;

    @Column(name = "updtd_by", nullable = false)
    private String updatedBy;
}
