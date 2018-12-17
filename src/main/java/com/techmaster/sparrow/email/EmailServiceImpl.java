package com.techmaster.sparrow.email;

import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.repositories.EmailContentRepo;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class EmailServiceImpl implements EmailService {

    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public RuleResultBean send(EmailContent emailContent) {
        logger.debug("Starting to send email: " + emailContent.getTemplate().getTemplateDescription());
        RuleResultBean resultBean = new RuleResultBean();
        if (emailContent.getTemplate().getAttachments().isEmpty()) {
            return sendSimple(emailContent, resultBean);
        } else {
            return sendWithAttachments(emailContent, resultBean);
        }
    }

    private RuleResultBean sendSimple(EmailContent emailContent, RuleResultBean resultBean) {
        SimpleMailMessage message = new SimpleMailMessage();
        EmailServiceHelper.setReceivers(message, emailContent, resultBean);
        EmailServiceHelper.setSubject(message, emailContent);
        String content = EmailServiceHelper.prepareSimpleContent(emailContent, resultBean);
        message.setText(content);
        emailSender.send(message);
        return resultBean;
    }

    private RuleResultBean sendWithAttachments(EmailContent emailContent, RuleResultBean resultBean) {
        Message mimeMultipart = EmailServiceHelper.prepareMimeMsg(emailSender, emailContent, resultBean);
        if (resultBean.isSuccess()) {
            try {
                Transport.send(mimeMultipart);
            } catch (MessagingException e) {
                SparrowUtil.logException(logger, e, "Application error occurred while trying to send email");
                resultBean.setApplicationError(e);
            }
        }
        return resultBean;
    }


}
