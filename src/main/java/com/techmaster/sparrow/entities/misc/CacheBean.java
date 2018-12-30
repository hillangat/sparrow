package com.techmaster.sparrow.entities.misc;


import com.techmaster.sparrow.converters.BooleanToYNStringConverter;
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
@Table(name = "CCH_BN")
public class CacheBean extends AuditInfoBean {

    @Id
    @Column(name = "NAM", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cch_bn_gen")
    @SequenceGenerator(name="cch_bn_gen", sequenceName = "cch_bn_seq", allocationSize=100)
    private String name;

    @Column(name = "KY", nullable = false)
    private String key;

    @Column(name = "FRSHNG", nullable = false)
    @Convert(converter = BooleanToYNStringConverter.class)
    private boolean refreshing;

    @Column(name = "SLCTD", nullable = false)
    @Convert(converter = BooleanToYNStringConverter.class)
    private boolean selected;

    @Column(name = "ACTV", nullable = false)
    @Convert(converter = BooleanToYNStringConverter.class)
    private boolean active;
}
