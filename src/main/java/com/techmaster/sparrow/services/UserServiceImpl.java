package com.techmaster.sparrow.services;

import com.techmaster.sparrow.entities.User;
import com.techmaster.sparrow.enums.RuleTypesEnums;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.repositories.SparrowJDBCExecutor;
import com.techmaster.sparrow.repositories.UserRepository;
import com.techmaster.sparrow.rules.abstracts.KnowledgeBaseFactory;
import com.techmaster.sparrow.rules.beans.DefaultRuleBean;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.validation.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService<UserRepository> {

    @Autowired private UserRepository userRepository;
    @Autowired private EntityManager entityManager;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserById(long userId) {
        return SparrowUtil.getIfExist(userRepository.findById(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return SparrowUtil.getListOf(userRepository.findAll());
    }

    @Override
    public UserRuleBean validate(User user) {
        UserRuleBean userRuleBean = new UserRuleBean();
        UserValidator.validateUserCreate(user, userRuleBean, userRepository);
        return userRuleBean;
    }

    @Override
    public UserRuleBean editUser(User user) {
        UserRuleBean userRuleBean = validate(user);
        if (userRuleBean != null && userRuleBean.isSuccess()) {
            userRepository.save(user);
        }
        return userRuleBean;
    }

    @Override
    public UserRuleBean createUser(User user) {
        UserRuleBean userRuleBean = validate(user);
        if (userRuleBean != null && userRuleBean.isSuccess()) {
            userRepository.save(user);
        }
        return userRuleBean;
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserRuleBean uploadProfilePic(long userId, Blob profilePic) {
        User user = SparrowUtil.getIfExist(userRepository.findById(userId));
        if (user != null ) {
            user.setProfilePic(profilePic);
            userRepository.save(user);
        }
        return SparrowUtil.clone(new UserRuleBean(), user);
    }

    @Override
    public StatusEnum lockUserAccount(long userId, long lockerUserId, String reason) {
        userRepository.lockUnlockUser("Y", userId, lockerUserId, reason);
        return StatusEnum.SUCCESS;
    }

    @Override
    public StatusEnum unlockUserAccount(long userId) {
        userRepository.lockUnlockUser("N", userId, 0, null);
        return StatusEnum.SUCCESS;
    }

    @Override
    public Map<String, Object> handleForgotPassword(String userName, String email, String phoneNumber) {
        return null;
    }

    @Override
    public UserRuleBean changeEmail(Map<String, Object> args) {

        UserRuleBean ruleBean = new UserRuleBean();

        try {

            Object userId = args.get("userId");
            Object email = args.get("email");

            UserValidator.validateEmail(ruleBean, email);
            UserValidator.validateUserId(ruleBean, userId, userRepository);

            if (ruleBean.isSuccess()) {
                List<Object> params = new ArrayList<>();
                params.add(email);
                params.add(userId);

                String queryStr = SparrowUtil.getQueryForSqlId("changeEmail");
                SparrowJDBCExecutor jdbcExecutor = SparrowBeanContext.getBean(SparrowJDBCExecutor.class);

                jdbcExecutor.executeUpdate(queryStr, params);

            } else {
                logger.error("Change email request failed validation: " + SparrowUtil.stringifyMap(ruleBean.getRuleResultBean().getErrors()));
            }
        } catch (Exception e) {
            logger.error("Application error while trying to  save email: " + e.getLocalizedMessage());
            ruleBean.getRuleResultBean().setError("applicationError", "Application error occurred!");
        }

        return ruleBean;
    }

    @Override
    public UserRuleBean changeUserName(Object userId, Object userName) {

        UserRuleBean ruleBean = new UserRuleBean();

        try {

            UserValidator.validateUserId(ruleBean, userId, userRepository);
            UserValidator.validateUserName(ruleBean, userName);
            UserValidator.validateExistingUserName(ruleBean, userName);

            if (ruleBean.isSuccess()) {

                List<Object> params = new ArrayList<>();
                params.add(userName);
                params.add(userId);

                String queryStr = SparrowUtil.getQueryForSqlId("changeUserName");
                SparrowJDBCExecutor jdbcExecutor = SparrowBeanContext.getBean(SparrowJDBCExecutor.class);

                jdbcExecutor.executeUpdate(queryStr, params);

            } else {
                logger.error("Change email request failed validation: " + SparrowUtil.stringifyMap(ruleBean.getRuleResultBean().getErrors()));
            }
        } catch (Exception e) {
            logger.error("Application error while trying to  save email: " + e.getLocalizedMessage());
            ruleBean.getRuleResultBean().setError("applicationError", "Application error occurred!");
        }

        return ruleBean;
    }

    @Override
    public StatusEnum deleteProfilePic(long userId) {
        userRepository.deleteUserProfilePic(userId);
        return StatusEnum.SUCCESS;
    }

    @Override
    public StatusEnum reportUser(long userId, long reportedBy, String reason) {
        return null;
    }

    @Override
    public UserRepository getRepository() {
        return userRepository;
    }
}
