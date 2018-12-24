package com.techmaster.sparrow.services;

import com.techmaster.sparrow.entities.UserRole;
import com.techmaster.sparrow.repositories.UserRepo;
import com.techmaster.sparrow.repositories.UserRoleRepo;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.hibernate.validator.internal.engine.groups.DefaultValidationOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired private UserRoleRepo userRoleRepo;
    @Autowired private UserRepo userRepo;

    @Override
    public RuleResultBean createOrUpdateRole(UserRole userRole) {
        RuleResultBean resultBean = new RuleResultBean();
        if (!SparrowUtil.notNullNotEmpty(userRole.getRoleName())) {
            resultBean.setError("roleName", "Role name is required");
        }
        if (resultBean.isSuccess()) {
            userRoleRepo.save(userRole);
        }
        return resultBean;
    }

    @Override
    public RuleResultBean deleteUserRole(long userRoleId, String userName) {

        RuleResultBean resultBean = new RuleResultBean();
        UserRole userRole = SparrowUtil.getIfExist(userRoleRepo.findById(userRoleId));

        if (userRole == null) {
            resultBean.setError("userRoleId", "User role of user Id = " + userRoleId + ", could not be found");
            return resultBean;
        }

        if (SparrowUtil.isAdmin(userRole)) {
            resultBean.setError("userRole", "Admin role cannot be deleted from the system");
            return resultBean;
        }

        if ( userName == null ) {
            resultBean.setError("userName", "You must be logged in to delete user role");
        }

        List<UserRole> userRoles = userRepo.getUserRoles(userName);

        if ( !SparrowUtil.isAdmin(userRoles) ) {
            resultBean.setError("userRole", "Only admin can delete a user role");
        }

        return resultBean;
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        List<UserRole> userRoles = SparrowUtil.getListOf(userRoleRepo.findAll());
        return userRoles;
    }

    @Override
    public UserRole getUserRoleById(long userRoleId) {
        UserRole userRole = SparrowUtil.getIfExist(userRoleRepo.findById(userRoleId));
        return userRole;
    }

    @Override
    public List<UserRole> getUserRoleByIds(List<Long> userRoleIds) {
        List<UserRole> userRoles = SparrowUtil.getListOf(userRoleRepo.findAllById(userRoleIds));
        return userRoles;
    }
}
