package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.repositories.UserRepository;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.services.UserService;
import com.techmaster.sparrow.util.SparrowUtil;

public class UserValidator {

    public static void validateEmail (UserRuleBean ruleBean, Object email) {

        if (!SparrowUtil.notNullNotEmpty(email)) {
            ruleBean.getRuleResultBean().setError("email", "Email is required.");
            return;
        }

        if (!SparrowUtil.validateEmail(email.toString())) {
            ruleBean.getRuleResultBean().setError("email", "Invalid email");
        }
    }

    public static void validateUserId (UserRuleBean ruleBean, Object userId, UserRepository repository) {

        if (!SparrowUtil.isNumeric(userId)) {
            ruleBean.getRuleResultBean().setError("userId", "Invalid User ID");
            return;
        }

        long id = SparrowUtil.getLongFromObject(userId);

        if (id < 1) {
            ruleBean.getRuleResultBean().setError("userId", "User ID is required");
            return;
        }

        if (!repository.existsById(id)) {
            ruleBean.getRuleResultBean().setError("userId", "User ID not found in the system");
            return;
        }

    }

}
