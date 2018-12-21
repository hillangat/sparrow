package com.techmaster.sparrow.entities.misc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "CMMNT")
public class Comment extends AuditInfoBean {

    @Id
    @Column(name = "CMMNT_ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private String commentId;

    @Column(name = "TXT", nullable = false)
    private String text;

    @Column(name = "BLCKD")
    private boolean blocked;

    @Column(name = "USR_ID", nullable = false)
    private String userId;

    @Column(name = "RPRTD")
    private String reported;

    @Column(name = "PRNT_ID")
    private String parentId;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn(name = "PRNT_ID")
    private List<Comment> replies;

}
