package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.config.SecurityService;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.entities.misc.User;
import com.techmaster.sparrow.entities.misc.UserRole;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.services.apis.UserService;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController extends BaseController {

    @Autowired private UserService userService;
    @Autowired private SecurityService securityService;

    @PostMapping("user/login")
    public ResponseEntity<ResponseData> login(@RequestBody Map<String, Object> reqParams) {

        String userName = SparrowUtil.getStringOrNullOfObj(reqParams.get("username"));
        String password = SparrowUtil.getStringOrNullOfObj(reqParams.get("password"));

       // securityService.autologin(userName, password);

        return getResponse(true, reqParams, null);
    }

    @GetMapping("user/logout")
    public ResponseEntity<ResponseData> logout() {
        securityService.logout();
        return getResponse(true, null, null);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<ResponseData> getUser(@PathVariable(value = "userId", required = false) Long userId) {
        Object data = userId == null || userId == 0 ? userService.getAllUsers() : userService.getUserById(userId);
        return getResponse(true, data, null);
    }

    @PostMapping("user")
    public ResponseEntity<ResponseData> saveOrUpdate(@RequestBody User user) {
        List<User> userList = new ArrayList<>(1);
        userList.add(user);
        UserRuleBean ruleBean = user.getUserId() > 0 ? userService.createUser(user) : userService.editUser(user);
        return getResponse(false, null, ruleBean.getRuleResultBean());
    }

    @PostMapping("user/email")
    public ResponseEntity<ResponseData> changeEmail(@RequestBody Map<String, Object> args) {
        UserRuleBean ruleBean = userService.changeEmail(args);
        return getResponse(false, null, ruleBean.getRuleResultBean());
    }

    @GetMapping("user/{userId}/email/confirm")
    public ResponseEntity<ResponseData> confirmEmail(@PathVariable("userId") Long userId) {
        RuleResultBean resultBean = userService.confirmEmail(userId);
        return getResponse(false, null, resultBean);
    }

    @PostMapping("user/userName")
    public ResponseEntity<ResponseData> changeUserName(@RequestBody Map<String, Object> args) {
        UserRuleBean ruleBean = userService.changeUserName(args.get("userId"), args.get("userName"));
        return getResponse(false, null, ruleBean.getRuleResultBean());
    }

    @GetMapping("user/{userId}/userRoles")
    public ResponseEntity<ResponseData> getUserRolesByUserId(@PathVariable(value = "userId", required = false) Long userId) {
        Set<UserRole> userRoles = userService.getUserRolesByUserId(userId);
        return getResponse(true, userRoles, null);
    }

    @DeleteMapping("user/{userId}/userRoles/{userRoleId}")
    public ResponseEntity<ResponseData> deleteUserRole(
            @PathVariable(value = "userId", required = false) Long userId,
            @PathVariable(value = "userRoleId", required = false) Long userRoleId ) {

        RuleResultBean resultBean = userService.removeUserRoleFromUser(userId, userRoleId);
        return getResponse(true, null, resultBean);
    }

    @PostMapping("user/{userId}/userRoles/{userRoleId}")
    public ResponseEntity<ResponseData> addRoleToUser(
            @PathVariable(value = "userId", required = false) Long userId,
            @PathVariable(value = "userRoleId", required = false) Long userRoleId ) {

        RuleResultBean resultBean = userService.addRoleToUser(userRoleId, userId);
        return getResponse(true, null, resultBean);
    }

}
