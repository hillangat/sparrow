package com.techmaster.sparrow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "CMMNT")
public class Comment extends AuditInfoBean {

    @Column(name = "TXT", nullable = false)
    private String text;

    @Column(name = "BLCKD")
    private boolean blocked;

    @Column(name = "USR_ID", nullable = false)
    private String userId;

    @Column(name = "RPRTD")
    private String reported;

    @ManyToOne
    private List<Comment> replies;

}
