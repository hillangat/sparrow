package com.techmaster.sparrow.entities.misc;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SearchResult {

    private Object data;
    private int pageNo;
    private int pageSize;
    private int total;

}
