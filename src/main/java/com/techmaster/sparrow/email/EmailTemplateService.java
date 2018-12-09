package com.techmaster.sparrow.email;

import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.entities.email.EmailTemplate;

import java.util.List;
import java.util.Map;

public interface EmailTemplateService {

    Map<String, Object> prepare(EmailTemplate template, List<EmailReceiver> receivers, String subject);
    String getBody();
    String getAttachments();
    String getGreeter();
    List<String> getReceivers();

}
