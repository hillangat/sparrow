package com.techmaster.sparrow.entities;

import lombok.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData implements Serializable {

    protected Object data;
    protected String message;
    protected String status;
    protected Map<String, List<String>> errors;
}
