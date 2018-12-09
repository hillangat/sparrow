package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.rules.beans.DefaultRuleBean;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.util.SparrowUtil;

public class DefaultValidator {

    public void validateNumber(DefaultRuleBean defaultRuleBean, Object obj, String key, String message) {
        if (!SparrowUtil.isNumeric(obj)) {
            defaultRuleBean.getRuleResultBean().setError(key, message);
        }
    }

}
