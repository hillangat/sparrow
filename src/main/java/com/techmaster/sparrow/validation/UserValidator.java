package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.misc.User;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.repositories.SparrowJDBCExecutor;
import com.techmaster.sparrow.repositories.UserRepository;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidator extends AbstractValidator {

    public void validateUserId (UserRuleBean ruleBean, Object userId, UserRepository repository) {

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

    public void validateUserName (UserRuleBean ruleBean, Object userName) {

        validateEmpty(ruleBean.getRuleResultBean(), userName, "userName", "Username cannot be empty");

        if (!ruleBean.isSuccess() &&
                ValidatorUtil.isSpecialChar(userName.toString())) {

            ruleBean.getRuleResultBean().setError("userName", "Username has special characters.");
        }
    }

    public void validateExistingUserName (UserRuleBean ruleBean, Object userName) {
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

    public void validateUserCreate(User user, UserRuleBean ruleBean, UserRepository repository) {

        // first name
        validateEmpty(ruleBean.getRuleResultBean(), user.getFirstName(), "firstName", "First name is required");
        validateSpecialChar(ruleBean.getRuleResultBean(), user.getFirstName(), "firstName", "Fist name has special characters");

        // last name
        validateEmpty(ruleBean.getRuleResultBean(), user.getLastName(), "lastName", "Last name is required");
        validateSpecialChar(ruleBean.getRuleResultBean(), user.getFirstName(), "lastName", "Last name has special characters");

        // user name
        validateUserName(ruleBean, user.getUserName());
        validateExistingUserName(ruleBean, user.getUserName());
        validateSpecialChar(ruleBean.getRuleResultBean(), user.getFirstName(), "userName", "User name has special characters");

        // email
        validateEmail(ruleBean.getRuleResultBean(), user.getEmail());

        // password
        validateEmpty(ruleBean.getRuleResultBean(), user.getLastName(), "lastName", "Last name is required");
        validateSpecialChar(ruleBean.getRuleResultBean(), user.getFirstName(), "lastName", "Last name has special characters");
        validateLength(5, 50, ruleBean.getRuleResultBean(), user.getPassword(), "password", "Password");

    }

}
