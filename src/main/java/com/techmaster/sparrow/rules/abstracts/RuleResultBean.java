package com.techmaster.sparrow.rules.abstracts;

import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.util.SparrowUtil;
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
        setError(SparrowConstants.APPLICATION_ERROR_KEY, "Application error occurred");
    }

    public void setApplicationError ( Throwable e ) {
        this.exceptionType = RuleExceptionType.APPLICATION;
        setError(SparrowConstants.APPLICATION_ERROR_KEY, e.getLocalizedMessage());
    }

    public boolean isSuccess() {
        return errors.isEmpty() &&
                status.equals(Status.SUCCESS);
    };

    public void extendTo( RuleResultBean destination ) {
        errors.entrySet().forEach(e -> {
            String key = e.getKey();
            List<String> val = e.getValue();
            if (SparrowUtil.isCollNotEmpty(destination.getErrors().get(key))) {
                destination.getErrors().get(key).addAll(val);
            } else {
                destination.getErrors().put(key, val);
            }
            if (!val.isEmpty()) {
                destination.setStatus(Status.FAILED);
            }
        });
    }
}
