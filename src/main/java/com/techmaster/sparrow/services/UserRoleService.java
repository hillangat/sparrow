package com.techmaster.sparrow.services;

import com.techmaster.sparrow.entities.misc.UserRole;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;

import java.util.List;

public interface UserRoleService {

    RuleResultBean createOrUpdateRole(UserRole userRole);
    RuleResultBean deleteUserRole(long userRoleId, String userName);
    List<UserRole> getAllUserRoles();
    UserRole getUserRoleById( long userRoleId );
    List<UserRole> getUserRoleByIds(List<Long> userRoleIds);
    UserRole getUserRoleByName( String roleName );

}
