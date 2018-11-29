package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.ResponseData;
import com.techmaster.sparrow.entities.User;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class UserController extends BaseController {

    @Autowired private UserRepository userRepository;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> getUsers(@PathVariable(required = false) Long userId) {
        Object data = userId == null ? userRepository.findAll() : userRepository.findById(userId);
        return ResponseEntity.ok(new ResponseData(data, StatusEnum.SUCCESS.getStatus(), SUCCESS_RETRIEVAL_MSG));
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> createUser(@PathVariable(required = false) Long userId) {
        User user = new User();
        user.setEmail("hillangat@gmail.com");
        user.setFirstName("Hillary");
        user.setLastName("Langat");
        user.setUserName("hillangat");
        user.setCreatedBy(getUserName());
        user.setUpdatedBy(getUserName());
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseData(user, StatusEnum.SUCCESS.getStatus(), SUCCESS_RETRIEVAL_MSG));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> createOrUpdate(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseData(null, StatusEnum.SUCCESS.getStatus(), SUCCESS_RETRIEVAL_MSG));
    }

}
