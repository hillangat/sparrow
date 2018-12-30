package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.services.apis.EmailContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController extends BaseController {

    @Autowired
    private EmailContentService emailContentService;

    @PostMapping("email")
    public ResponseEntity<ResponseData> saveEmail(@RequestBody EmailContent emailContent) {
        RuleResultBean resultBean = emailContent.getContentId() == 0
                ? emailContentService.save(emailContent) : emailContentService.edit(emailContent);
        return getResponse(false, emailContent, resultBean);
    }

    @DeleteMapping("email/{emailId}")
    public ResponseEntity<ResponseData> deleteEmail(@PathVariable("emailId") Long emailId) {
        RuleResultBean resultBean = emailContentService.deleteEmailContent(emailId);
        return getResponse(false, null, resultBean);
    }

    @GetMapping("email/{emailId}/send")
    public ResponseEntity<ResponseData> sendEmail(@PathVariable("emailId") Long emailId) {
        RuleResultBean ruleResultBean = emailContentService.send(emailId);
        return getResponse(false, null, ruleResultBean);
    }

}
