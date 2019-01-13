package com.techmaster.sparrow.entities.misc;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CLB")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Club extends AuditInfoBean {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clb_gen")
    @SequenceGenerator(name="clb_gen", sequenceName = "clb_seq", allocationSize=100)
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "NAM", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "LCTN_ID")
    private Location location;


}
