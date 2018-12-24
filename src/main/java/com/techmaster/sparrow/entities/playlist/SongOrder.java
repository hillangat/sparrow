package com.techmaster.sparrow.entities.playlist;

import com.techmaster.sparrow.entities.misc.AuditInfoBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "SNG_ORDR")
public class SongOrder extends AuditInfoBean {

    @Id
    @Column(name = "SNG_ORDR_ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private long songOrderId;

    @JoinColumn(name = "SNG_ID")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Song song;

    @Column(name = "SNG_INDX", nullable = false)
    private int songIndex;

    @Column(name = "USR_ID", nullable = false)
    private long userId;



}
