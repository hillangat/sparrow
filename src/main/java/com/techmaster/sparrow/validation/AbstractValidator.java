package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.rules.beans.DefaultRuleBean;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.util.SparrowUtil;

public abstract class AbstractValidator {

    public void validateNumber(DefaultRuleBean defaultRuleBean, Object obj, String key, String message) {
        if (!SparrowUtil.isNumeric(obj)) {
            defaultRuleBean.getRuleResultBean().setError(key, message);
        }
    }

    public void validateEmail (RuleResultBean ruleBean, Object email) {

        if (!SparrowUtil.notNullNotEmpty(email)) {
            ruleBean.setError("email", "Email is required.");
            return;
        }

        if (!SparrowUtil.validateEmail(email.toString())) {
            ruleBean.setError("email", "Invalid email");
        }
    }

    public void validateSpecialChar(RuleResultBean ruleBean, String obj, String key, String message) {
        if (obj != null && ValidatorUtil.isSpecialChar(obj)) {
            ruleBean.setError(key, message);
        }
    }

    public void validateLength (int min, int max, RuleResultBean ruleBean, String obj, String key, String uiName) {

        String msg = uiName + " must have between " + min + " and " + max + " number of characters";

        if (!SparrowUtil.notNullNotEmpty(obj) ||
                obj.length() < min ||
                obj.length() > max) {

            ruleBean.setError(key, msg);
        }
    }

}
