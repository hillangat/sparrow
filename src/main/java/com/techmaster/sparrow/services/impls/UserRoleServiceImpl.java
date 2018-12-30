package com.techmaster.sparrow.services.impls;

import com.techmaster.sparrow.entities.misc.UserRole;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.repositories.UserRepo;
import com.techmaster.sparrow.repositories.UserRoleRepo;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.services.apis.UserRoleService;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

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

        Set<UserRole> userRoles = userRepo.getUserRoles(userName);

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

    @Override
    public UserRole getUserRoleByName(String roleName) {
        String sql = "SELECT r FROM UserRole r WHERE r.roleName = ?";
        EntityManager entityManager = SparrowBeanContext.getEntityManager();
        Query query = entityManager.createQuery(sql).setParameter(1, roleName);
        query.setFirstResult(0);
        query.setMaxResults(1);
        UserRole userRole = (UserRole) query.getSingleResult();
        return userRole;
    }
}
