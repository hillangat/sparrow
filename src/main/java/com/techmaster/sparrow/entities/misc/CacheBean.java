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
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "NAM", nullable = false)
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
