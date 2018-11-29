package com.techmaster.sparrow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ResponseData implements Serializable {

    private Object data;
    private String message;
    private String status;
}
