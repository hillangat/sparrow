package com.techmaster.sparrow.services;

import com.techmaster.sparrow.entities.misc.User;
import com.techmaster.sparrow.entities.misc.UserRole;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.enums.UserDisableReasonType;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.repositories.SparrowJDBCExecutor;
import com.techmaster.sparrow.repositories.UserRepo;
import com.techmaster.sparrow.repositories.UserRoleRepo;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.validation.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService<UserRepo> {

    @Autowired private UserRepo userRepo;
    @Autowired private UserValidator userValidator;
    @Autowired private UserRoleRepo userRoleRepo;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserById(long userId) {
        return SparrowUtil.getIfExist(userRepo.findById(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return SparrowUtil.getListOf(userRepo.findAll());
    }

    @Override
    public UserRuleBean validate(User user) {
        UserRuleBean userRuleBean = new UserRuleBean();
        userValidator.validateUserCreate(user, userRuleBean, userRepo);
        return userRuleBean;
    }

    @Override
    public UserRuleBean editUser(User user) {
        UserRuleBean userRuleBean = validate(user);
        if (userRuleBean != null && userRuleBean.isSuccess()) {
            userRepo.save(user);
        }
        return userRuleBean;
    }

    @Override
    public UserRuleBean createUser(User user) {
        UserRuleBean userRuleBean = validate(user);
        if (userRuleBean != null && userRuleBean.isSuccess()) {
            userRepo.save(user);
        }
        return userRuleBean;
    }

    @Override
    public void deleteUser(long userId) {
        userRepo.deleteById(userId);
    }

    @Override
    public UserRuleBean uploadProfilePic(long userId, Blob profilePic) {
        User user = SparrowUtil.getIfExist(userRepo.findById(userId));
        if (user != null ) {
            user.setProfilePic(profilePic);
            userRepo.save(user);
        }
        return SparrowUtil.clone(new UserRuleBean(), user);
    }

    @Override
    public Status lockUserAccount(long userId, long lockerUserId, String reason) {
        userRepo.lockUnlockUser("Y", userId, lockerUserId, reason);
        return Status.SUCCESS;
    }

    @Override
    public Status unlockUserAccount(long userId) {
        userRepo.lockUnlockUser("N", userId, 0, null);
        return Status.SUCCESS;
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

            userValidator.validateEmail(ruleBean.getRuleResultBean(), email);
            userValidator.validateUserId(ruleBean, userId, userRepo);

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
    public RuleResultBean confirmEmail(Long userId) {
        RuleResultBean resultBean = userValidator.validateConfirmedEmail(userId);
        if (resultBean.isSuccess()) {
            userRepo.confirmEmail(userId);
        }
        return resultBean;
    }

    @Override
    public UserRuleBean changeUserName(Object userId, Object userName) {

        UserRuleBean ruleBean = new UserRuleBean();

        try {

            userValidator.validateUserId(ruleBean, userId, userRepo);
            userValidator.validateUserName(ruleBean, userName);
            userValidator.validateExistingUserName(ruleBean, userName);

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
    public Status deleteProfilePic(long userId) {
        userRepo.deleteUserProfilePic(userId);
        return Status.SUCCESS;
    }

    @Override
    public Status reportUser(long userId, long reportedBy, String reason) {
        return null;
    }

    @Override
    public UserRepo getRepository() {
        return userRepo;
    }

    @Override
    public Long getMaxUserId() {
        return userRepo.getMaxUserId();
    }

    @Override
    public Set<UserRole> getUserRolesByUserId(long userId) {
        Set<UserRole> userRoles = userRepo.getUserRolesByUserId(userId);
        return userRoles;
    }

    @Override
    public Set<UserRole> getUserRolesByUserName(String userName) {
        Set<UserRole> userRoles = userRepo.getUserRoles(userName);
        return userRoles;
    }

    @Override
    public RuleResultBean addRoleToUser(long roleId, long userId) {

        RuleResultBean resultBean = new RuleResultBean();

        try {

            User user = getUserById(userId);
            UserRole userRole = SparrowUtil.getIfExist(userRoleRepo.findById(roleId));

            if (userRole == null) {
                resultBean.setError("userRoles", "User role with Id: " + roleId + ", could not be found");
            } else {
                user.getUserRoles().add(userRole);
                userRepo.save(user);
            }

        } catch (Exception e) {
            String msg = "Application error occurred while trying to add role to user, roleId: " + roleId +" from userId: " + userId;
            SparrowUtil.logException(logger, e, msg);
            resultBean.setApplicationError(e);
        }

        return resultBean;
    }

    @Override
    public RuleResultBean removeUserRoleFromUser(long userId, long userRoleId) {

        RuleResultBean resultBean = new RuleResultBean();

        try {
            User user = getUserById(userId);

            Set<UserRole> userRoles = user.getUserRoles().stream()
                    .filter(u -> u.getRoleId() != userRoleId).collect(Collectors.toSet());

            if (userRoles.isEmpty()) {
                user.setActive(false);
                user.setDisableReasonType(UserDisableReasonType.USER_ROLES);
                logger.debug("All roles removed from the user. user disabled");
            }

            user.setUserRoles(userRoles);
            userRepo.save(user);

        } catch (Exception e) {
            String msg = "Application error occurred while trying to remove role: " + userRoleId +" from userId: " + userId;
            SparrowUtil.logException(logger, e, msg);
            resultBean.setApplicationError(e);
        }

        return resultBean;
    }
}
