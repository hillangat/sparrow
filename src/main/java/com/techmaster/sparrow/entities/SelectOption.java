package com.techmaster.sparrow.entities;

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
