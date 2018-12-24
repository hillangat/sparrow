package com.techmaster.sparrow.entities.playlist;

import lombok.*;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SongOrderAverage {

    private double average;
    private int ratingCount;
    private long songId;
    private double index;

}
