package com.techmaster.sparrow.services;

import com.techmaster.sparrow.constants.SparrowConstants;
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
        return null;
    }

    @Override
    public EmailContent save(EmailContent emailContent) {
        return null;
    }

    @Override
    public EmailContent edit(EmailContent emailContent) {
        return null;
    }

    @Override
    public RuleResultBean deleteEmailContent(long emailContentId) {
        return null;
    }

    @Override
    public RuleResultBean setStatus(long emailContentId, Status status) {
        return null;
    }

    @Override
    public RuleResultBean send(long emailContentId) {

        RuleResultBean resultBean = new RuleResultBean();
        EmailContent emailContent = SparrowUtil.getIfExist(emailContentRepo.findById(emailContentId));

        if (emailContent != null) {
            RuleResultBean sendErrors = emailService.send(emailContent);
            sendErrors.extendTo(resultBean);
        } else {
            resultBean.setApplicationError(RuleExceptionType.APPLICATION);
            logger.error("Email content not found for ID: " + emailContentId);
        }

        return resultBean;
    }
}
