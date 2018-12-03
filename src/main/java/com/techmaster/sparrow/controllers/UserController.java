package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.ResponseData;
import com.techmaster.sparrow.entities.User;
import com.techmaster.sparrow.entities.ValidationResponseData;
import com.techmaster.sparrow.enums.RuleTypesEnums;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.repositories.UserRepository;
import com.techmaster.sparrow.rules.abstracts.KnowledgeBaseFactory;
import com.techmaster.sparrow.rules.beans.UserRuleBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController extends BaseController {

    @Autowired private UserRepository userRepository;

    @GetMapping("user/{userId}")
    public ResponseEntity<ResponseData> getUser(@PathVariable(value = "userId", required = false) Long userId) {
        Object data = userId == null ? userRepository.findAll() : userRepository.findById(userId);
        return ResponseEntity.ok(new ResponseData(data, StatusEnum.SUCCESS.getStatus(), SUCCESS_RETRIEVAL_MSG, null));
    }

    @PostMapping("user")
    public ResponseEntity<ResponseData> saveOrUpdate(@RequestBody User user) {

        List<User> userList = new ArrayList<>(1);
        userList.add(user);

        List<UserRuleBean> ruleBeans = KnowledgeBaseFactory.getHelper(RuleTypesEnums.USER).runRules(userList);
        boolean success = ruleBeans != null && !ruleBeans.isEmpty() && ruleBeans.get(0).isSuccess();
        Map<String, List<String>> errors = success ? null : ruleBeans.get(0).getRuleResultBean().getErrors();

        String status = success ? StatusEnum.SUCCESS.getStatus() : StatusEnum.FAILED.getStatus();
        String msg = success ? SUCCESS_SAVED_MSG : FAILED_VALIDATION_MSG;

        if (success) {
            userRepository.save(user);
        }

        return ResponseEntity.ok(new ResponseData(user, msg, status, errors));
    }

}
