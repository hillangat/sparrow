package com.techmaster.sparrow.services;

import com.techmaster.sparrow.entities.User;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.rules.beans.DefaultRuleBean;
import com.techmaster.sparrow.rules.beans.UserRuleBean;

import java.sql.Blob;
import java.util.List;
import java.util.Map;

public interface UserService<T> extends RepositoryService<T> {

    User getUserById( long userId );
    List<User> getAllUsers();
    UserRuleBean validate( User user );
    UserRuleBean editUser(User user);
    UserRuleBean createUser(User user);
    void deleteUser(long userId);
    UserRuleBean uploadProfilePic(long userId, Blob profilePic);
    StatusEnum lockUserAccount(long userId, long lockerUserId, String message);
    StatusEnum unlockUserAccount(long userId);
    Map<String, Object> handleForgotPassword(String userName, String email, String phoneNumber);
    UserRuleBean changeEmail(Map<String, Object> args);
    UserRuleBean changeUserName(Object userId, Object userName);
    StatusEnum deleteProfilePic(long userId);
    StatusEnum reportUser(long userId, long reportedBy, String reason);
    Long getMaxUserId();


}
