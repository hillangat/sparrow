package com.techmaster.sparrow.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract class AuditInfoBean {
    private Date createDate;
    private Date lastUpdate;
    private String createdBy;
    private String updatedBy;
}
