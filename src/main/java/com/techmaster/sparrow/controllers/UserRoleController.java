package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.misc.UserRole;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.services.UserRoleService;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRoleController extends BaseController {

    @Autowired private UserRoleService userRoleService;

    @PostMapping("userRole")
    private ResponseEntity<ResponseData> createOrUpdateUserRole(@RequestBody UserRole userRole) {
        SparrowUtil.addAuditInfo(userRole, getUserName());
        RuleResultBean resultBean = userRoleService.createOrUpdateRole(userRole);
        return getResponse(false, userRole, resultBean);
    }

    @DeleteMapping("userRole/{userRoleId}")
    private ResponseEntity<ResponseData> deleteUserRole(@PathVariable("userRoleId") Long userRoleId) {
        RuleResultBean resultBean = userRoleService.deleteUserRole(userRoleId, getUserName());
        return getResponse(false, null, resultBean);
    }

    @GetMapping("userRole/{userRoleId}")
    private ResponseEntity<ResponseData> getUserRole(@PathVariable("userRoleId") Long userRoleId) {
        Object data = userRoleId == null || userRoleId == 0
                ? userRoleService.getAllUserRoles() :  userRoleService.getUserRoleById(userRoleId);
        return getResponse(false, data, null);
    }

}
