package com.techmaster.sparrow.services;

import com.techmaster.sparrow.email.EmailService;
import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.repositories.EmailContentRepo;
import com.techmaster.sparrow.rules.abstracts.RuleExceptionType;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.validation.EmailContentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailContentServiceImpl implements EmailContentService {

    @Autowired private EmailContentRepo emailContentRepo;
    @Autowired private EmailContentValidator emailContentValidator;
    @Autowired private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ResponseData search(GridDataQueryReq queryReq) {
        return null;
    }

    @Override
    public EmailContent getById(long emailContentId) {
        EmailContent emailContent = SparrowUtil.getIfExist(emailContentRepo.findById(emailContentId));
        return emailContent;
    }

    @Override
    public RuleResultBean save(EmailContent emailContent) {
        RuleResultBean resultBean = emailContentValidator.validateCreate(emailContent);
        emailContentRepo.save(emailContent);
        return resultBean;
    }

    @Override
    public RuleResultBean edit(EmailContent emailContent) {
        RuleResultBean resultBean = emailContentValidator.validateCreate(emailContent);
        emailContentRepo.save(emailContent);
        return resultBean;
    }

    @Override
    public RuleResultBean deleteEmailContent(long emailContentId) {
        EmailContent emailContent = SparrowUtil.getIfExist(emailContentRepo.findById(emailContentId));
        RuleResultBean resultBean = emailContentValidator.validateDelete(emailContent);
        return resultBean;
    }

    @Override
    public RuleResultBean setLifeStatus(long emailContentId, Status status) {
        EmailContent emailContent = SparrowUtil.getIfExist(emailContentRepo.findById(emailContentId));
        RuleResultBean resultBean = emailContentValidator.validateLifeStatusChange(emailContent, status);
        if (resultBean.isSuccess()) {
            emailContentRepo.updateLifeStatus(emailContentId, status);
        }
        return resultBean;
    }

    @Override
    public RuleResultBean setDeliveryStatus(long emailContentId, Status status) {
        EmailContent emailContent = SparrowUtil.getIfExist(emailContentRepo.findById(emailContentId));
        RuleResultBean resultBean = emailContentValidator.validateDeliveryStatusChange(emailContent, status);
        return resultBean;
    }

    @Override
    public RuleResultBean send(long emailContentId) {

        RuleResultBean resultBean = new RuleResultBean();
        EmailContent emailContent = SparrowUtil.getIfExist(emailContentRepo.findById(emailContentId));

        if (emailContent != null) {
            RuleResultBean sendErrors = emailService.send(emailContent);
            return sendErrors;
        } else {
            String msg = "Email content not found for ID: " + emailContentId;
            resultBean.setError("email", msg);
            logger.error(msg);
        }

        return resultBean;
    }
}
