package com.techmaster.sparrow.entities.misc;

import com.techmaster.sparrow.enums.RatingType;
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
@Table(name = "RTNG")
public class Rating extends AuditInfoBean {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "RTNG_ID", nullable = false)
    private long ratingId;

    @Column(name = "RTNG", nullable = false)
    private int rating;

    @Column(name = "USR_ID", nullable = false)
    private long userId;

    @Column(name = "RTNG_TYP", nullable = false)
    @Enumerated(EnumType.STRING)
    private RatingType ratingType;

}
