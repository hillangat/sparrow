package com.techmaster.sparrow.rules.abstracts;

import com.techmaster.sparrow.enums.StatusEnum;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString(callSuper = true)
public class RuleResultBean {

    private StatusEnum status = StatusEnum.SUCCESS;
    private Map<String, List<String>> errors = new HashMap<>();

    public void setError(String field, String error) {
        List<String> fieldErrors = errors.get(field);
        fieldErrors = fieldErrors == null ? new ArrayList<>() : fieldErrors;
        fieldErrors.add(error);
        errors.put(field, fieldErrors);
        status = StatusEnum.FAILED;
    }

    public void setApplicationError () {
        setError("applicationError", "Application error occurred");
    }
}
