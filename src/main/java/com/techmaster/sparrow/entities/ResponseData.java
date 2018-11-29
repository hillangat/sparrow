package com.techmaster.sparrow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class ResponseData implements Serializable {

    private Object data;
    private String message;
    private String status;

    public ResponseData(Object data, String status, String message) {
        this.data = data;
        this.message = message;
        this.status = status;
    }
}
