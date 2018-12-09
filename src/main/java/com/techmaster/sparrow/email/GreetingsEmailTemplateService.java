package com.techmaster.sparrow.email;

import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.entities.email.EmailTemplate;

import java.util.List;
import java.util.Map;

public class GreetingsEmailTemplateService implements EmailTemplateService {

    private EmailTemplate emailTemplate;

    @Override
    public Map<String, Object> prepare(EmailTemplate template, List<EmailReceiver> receivers, String subject) {
        return null;
    }

    @Override
    public String getGreeter() {
        return null;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public String getAttachments() {
        return null;
    }

    @Override
    public List<String> getReceivers() {
        return null;
    }
}
