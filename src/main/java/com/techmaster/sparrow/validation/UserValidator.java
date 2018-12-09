package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.User;
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

    public static void validateSpecialChar(UserRuleBean ruleBean, String obj, String key, String message) {
        if (obj != null && ValidatorUtil.isSpecialChar(obj)) {
            ruleBean.getRuleResultBean().setError(key, message);
        }
    }

    public static void validateLength (int min, int max, UserRuleBean ruleBean, String obj, String key, String uiName) {

        String msg = uiName + " must have between " + min + " and " + max + " number of characters";

        if (!SparrowUtil.notNullNotEmpty(obj) ||
                obj.toString().length() < min ||
                obj.toString().length() > max) {

            ruleBean.getRuleResultBean().setError(key, msg);
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

    public static void validateUserCreate(User user, UserRuleBean ruleBean, UserRepository repository) {

        // first name
        validateEmpty(ruleBean, user.getFirstName(), "firstName", "First name is required");
        validateSpecialChar(ruleBean, user.getFirstName(), "firstName", "Fist name has special characters");

        // last name
        validateEmpty(ruleBean, user.getLastName(), "lastName", "Last name is required");
        validateSpecialChar(ruleBean, user.getFirstName(), "lastName", "Last name has special characters");

        // user name
        validateUserName(ruleBean, user.getUserName());
        validateExistingUserName(ruleBean, user.getUserName());
        validateSpecialChar(ruleBean, user.getFirstName(), "userName", "User name has special characters");

        // email
        validateEmail(ruleBean, user.getEmail());

        // password
        validateEmpty(ruleBean, user.getLastName(), "lastName", "Last name is required");
        validateSpecialChar(ruleBean, user.getFirstName(), "lastName", "Last name has special characters");
        validateLength(5, 50, ruleBean, user.getPassword(), "password", "Password");

    }

}
