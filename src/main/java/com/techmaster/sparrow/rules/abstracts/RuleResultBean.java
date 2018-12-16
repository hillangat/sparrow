package com.techmaster.sparrow.rules.abstracts;

import com.techmaster.sparrow.enums.Status;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString(callSuper = true)
public class RuleResultBean {

    private Status status = Status.SUCCESS;
    private Map<String, List<String>> errors = new HashMap<>();
    private RuleExceptionType exceptionType;

    public void setError(String field, String error) {
        List<String> fieldErrors = errors.get(field);
        fieldErrors = fieldErrors == null ? new ArrayList<>() : fieldErrors;
        fieldErrors.add(error);
        errors.put(field, fieldErrors);
        status = Status.FAILED;
    }

    public void setApplicationError ( RuleExceptionType exceptionType ) {
        this.exceptionType = exceptionType;
        setError("applicationError", "Application error occurred");
    }

    public boolean isSuccess() {
        return errors.isEmpty() &&
                status.equals(Status.SUCCESS);
    };
}
