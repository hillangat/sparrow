package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.entities.misc.User;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.services.UserService;
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
        return ResponseEntity.ok(new ResponseData(data, Status.SUCCESS.getStatus(), SUCCESS_RETRIEVAL_MSG, null, 1));
    }

    @PostMapping("user")
    public ResponseEntity<ResponseData> saveOrUpdate(@RequestBody User user) {

        List<User> userList = new ArrayList<>(1);
        userList.add(user);

        UserRuleBean ruleBean = user.getUserId() > 0 ? userService.createUser(user) : userService.editUser(user);
        boolean success = ruleBean != null && ruleBean.isSuccess();
        Map<String, List<String>> errors = success ? null : ruleBean.getRuleResultBean().getErrors();

        String status = success ? Status.SUCCESS.getStatus() : Status.FAILED.getStatus();
        String msg = success ? SUCCESS_SAVED_MSG : FAILED_VALIDATION_MSG;

        return ResponseEntity.ok(new ResponseData(user, msg, status, errors, 1));
    }

    @PostMapping("user/email")
    public ResponseEntity<ResponseData> changeEmail(@RequestBody Map<String, Object> args) {

        UserRuleBean ruleBean = userService.changeEmail(args);
        String status = ruleBean.isSuccess() ? Status.SUCCESS.getStatus() : Status.FAILED.getStatus();

        String otherError = ruleBean.getRuleResultBean().getErrors().containsKey(SparrowConstants.APPLICATION_ERROR_KEY)
                ? APPLICATION_ERROR_OCCURRED : FAILED_VALIDATION_MSG;

        String msg = ruleBean.isSuccess() ? SUCCESS_SAVED_MSG : otherError;

        return ResponseEntity.ok(new ResponseData(null, msg, status, ruleBean.getRuleResultBean().getErrors(), 0));
    }

    @PostMapping("user/userName")
    public ResponseEntity<ResponseData> changeUserName(@RequestBody Map<String, Object> args) {

        UserRuleBean ruleBean = userService.changeUserName(args.get("userId"), args.get("userName"));
        String status = ruleBean.isSuccess() ? Status.SUCCESS.getStatus() : Status.FAILED.getStatus();

        String otherError = ruleBean.getRuleResultBean().getErrors().containsKey(SparrowConstants.APPLICATION_ERROR_KEY)
                ? APPLICATION_ERROR_OCCURRED : FAILED_VALIDATION_MSG;

        String msg = ruleBean.isSuccess() ? SUCCESS_SAVED_MSG : otherError;

        return ResponseEntity.ok(new ResponseData(null, msg, status, ruleBean.getRuleResultBean().getErrors(), 0));
    }

}
