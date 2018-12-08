package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.repositories.SparrowJDBCExecutor;
import com.techmaster.sparrow.repositories.UserRepository;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.services.UserService;
import com.techmaster.sparrow.util.SparrowUtil;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {

    public static void validateEmpty (UserRuleBean userRuleBean, Object obj, String key, String message) {
        if (!SparrowUtil.notNullNotEmpty(obj)) {
            userRuleBean.getRuleResultBean().setError(key, message);
        }
    }

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

    public static void validateUserName (UserRuleBean ruleBean, Object userName) {

        validateEmpty(ruleBean, userName, "userName", "Username cannot be empty");

        if (!ruleBean.isSuccess() &&
                ValidatorUtil.isSpecialChar(userName.toString())) {

            ruleBean.getRuleResultBean().setError("userName", "Username has special characters.");
        }
    }

    public static void validateExistingUserName (UserRuleBean ruleBean, Object userName) {
        SparrowJDBCExecutor jdbcExecutor = SparrowBeanContext.getBean(SparrowJDBCExecutor.class);
        if (jdbcExecutor != null) {
            String query = SparrowUtil.getQueryForSqlId("getUserNameCount");
            List<Object> userNames = new ArrayList<>();
            userNames.add(userName);
            Object count = jdbcExecutor.executeQueryForOneReturn(query, userNames);
            if (SparrowUtil.notNullNotEmpty(count) && SparrowUtil.getLongFromObject(count) > 0) {
                ruleBean.getRuleResultBean().setError("userName", "User name is already taken");
            }
        } else {
            ruleBean.getRuleResultBean().setError(SparrowConstants.APPLICATION_ERROR_KEY, "Cannot connect to database");
        }
    }

}
