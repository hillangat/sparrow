package com.techmaster.sparrow.email;

import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.enums.EmailReasonType;
import com.techmaster.sparrow.repositories.EmailContentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;
    @Autowired
    public EmailContentRepo emailContentRepo;
    @Autowired
    public EmailTemplateService emailTemplateService;


    @Override
    public void send(EmailContent emailContent) {

    }

}
