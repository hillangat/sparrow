package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import org.springframework.stereotype.Component;

@Component
public class EmailContentValidator extends AbstractValidator {

    RuleResultBean validateCreate(EmailContent emailContent) {
        RuleResultBean resultBean = new RuleResultBean();
        return resultBean;
    }

    RuleResultBean validateReviewStatusChange(EmailContent emailContent, Status toStatus) {

        Status lifeStatus = emailContent.getLifeStatus();
        Status delStatus = emailContent.getDeliveryStatus();

        if (toStatus.equals(Status.APPROVED)) {

        }

        RuleResultBean resultBean = new RuleResultBean();
        return resultBean;
    }

}
