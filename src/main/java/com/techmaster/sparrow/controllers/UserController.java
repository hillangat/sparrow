package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.SparrowResponseData;
import com.techmaster.sparrow.entities.User;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Autowired private UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<SparrowResponseData> getUsers(@PathVariable(required = false) Long userId) {
        Object data = userId == null ? userRepository.findAll() : userRepository.findById(userId);
        return ResponseEntity.ok(new SparrowResponseData(data, StatusEnum.SUCCESS.getStatus(), SUCCESS_RETRIEVAL_MSG));
    }

    @PostMapping()
    public ResponseEntity<SparrowResponseData> createOrUpdate(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.ok(new SparrowResponseData(null, StatusEnum.SUCCESS.getStatus(), SUCCESS_RETRIEVAL_MSG));
    }

}
