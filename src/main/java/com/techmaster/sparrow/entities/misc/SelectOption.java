package com.techmaster.sparrow.entities.misc;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SelectOption {
    private String value;
    private String text;
}
