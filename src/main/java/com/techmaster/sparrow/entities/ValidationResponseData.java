package com.techmaster.sparrow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor()
public class ValidationResponseData extends ResponseData {

    private Map<String, List<String>> errors = new HashMap<>();

    public ValidationResponseData create(Object data, String message, String status) {
        this.data = data;
        this.message = message;
        this.status = status;
        return this;
    }
}
