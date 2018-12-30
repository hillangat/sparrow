package com.techmaster.sparrow.services.apis;

import com.techmaster.sparrow.entities.misc.User;
import com.techmaster.sparrow.entities.misc.UserRole;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.services.RepositoryService;

import java.sql.Blob;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {

    User getUserById( long userId );
    List<User> getAllUsers();
    UserRuleBean validate( User user );
    UserRuleBean editUser(User user);
    UserRuleBean createUser(User user);
    void deleteUser(long userId);
    UserRuleBean uploadProfilePic(long userId, Blob profilePic);
    Status lockUserAccount(long userId, long lockerUserId, String message);
    Status unlockUserAccount(long userId);
    Map<String, Object> handleForgotPassword(String userName, String email, String phoneNumber);
    UserRuleBean changeEmail(Map<String, Object> args);
    RuleResultBean confirmEmail(Long userId);
    UserRuleBean changeUserName(Object userId, Object userName);
    Status deleteProfilePic(long userId);
    Status reportUser(long userId, long reportedBy, String reason);
    Long getMaxUserId();
    Set<UserRole> getUserRolesByUserId(long userId);
    Set<UserRole> getUserRolesByUserName(String userName);
    RuleResultBean removeUserRoleFromUser(long userId, long userRoleId);
    RuleResultBean addRoleToUser(long roleId, long userId);


}
