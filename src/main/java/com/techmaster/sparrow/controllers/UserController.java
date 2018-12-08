package com.techmaster.sparrow.controllers;

import com.sun.deploy.security.ruleset.DefaultRule;
import com.techmaster.sparrow.entities.ResponseData;
import com.techmaster.sparrow.entities.User;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.rules.beans.DefaultRuleBean;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.services.RepositoryService;
import com.techmaster.sparrow.services.UserService;
import com.techmaster.sparrow.services.UserServiceImpl;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController extends BaseController {

    @Autowired private UserService userService;

    @GetMapping("user/{userId}")
    public ResponseEntity<ResponseData> getUser(@PathVariable(value = "userId", required = false) Long userId) {
        Object data = userId == null || userId == 0 ? userService.getAllUsers() : userService.getUserById(userId);
        return ResponseEntity.ok(new ResponseData(data, StatusEnum.SUCCESS.getStatus(), SUCCESS_RETRIEVAL_MSG, null));
    }

    @PostMapping("user")
    public ResponseEntity<ResponseData> saveOrUpdate(@RequestBody User user) {

        List<User> userList = new ArrayList<>(1);
        userList.add(user);

        UserRuleBean ruleBean = user.getUserId() > 0 ? userService.createUser(user) : userService.editUser(user);
        boolean success = ruleBean != null && ruleBean.isSuccess();
        Map<String, List<String>> errors = success ? null : ruleBean.getRuleResultBean().getErrors();

        String status = success ? StatusEnum.SUCCESS.getStatus() : StatusEnum.FAILED.getStatus();
        String msg = success ? SUCCESS_SAVED_MSG : FAILED_VALIDATION_MSG;

        return ResponseEntity.ok(new ResponseData(user, msg, status, errors));
    }

    @PostMapping("user/email")
    public ResponseEntity<ResponseData> changeEmail(@RequestBody Map<String, Object> args) {

        UserRuleBean ruleBean = userService.changeEmail(args);
        String status = ruleBean.isSuccess() ? StatusEnum.SUCCESS.getStatus() : StatusEnum.FAILED.getStatus();

        String otherError = ruleBean.getRuleResultBean().getErrors().containsKey("applicationError")
                ? APPLICATION_ERROR_OCCURRED : FAILED_VALIDATION_MSG;

        String msg = ruleBean.isSuccess() ? SUCCESS_SAVED_MSG : otherError;

        return ResponseEntity.ok(new ResponseData(null, msg, status, ruleBean.getRuleResultBean().getErrors()));
    }

}
