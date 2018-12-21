package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.entities.email.EmailTemplate;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * CONCEPTUAL, REVIEW, APPROVED OR REJECTED
 */

@Component
public class EmailContentValidator extends AbstractValidator {

    public RuleResultBean validateDelete(EmailContent emailContent) {
        RuleResultBean resultBean = new RuleResultBean();
        if (Status.DRAFT.equals(emailContent.getDeliveryStatus())) {
            resultBean.setError("delete", "Email is not in draft status");
        }
        return resultBean;
    }

    public RuleResultBean validateCreate(EmailContent emailContent) {
        RuleResultBean resultBean = new RuleResultBean();
        return resultBean;
    }

    private void validateTemplate (EmailTemplate emailTemplate, RuleResultBean resultBean) {
        if (emailTemplate == null) {
            resultBean.setError("emailTemplate", "Email template cannot be null");
            return;
        }

        if (emailTemplate.getContentBody() == null) {
            resultBean.setError("emailTemplate", "Email template content cannot be null");
        }

        String toList = emailTemplate.getToList() == null ?
                null : SparrowUtil.getBlobStr(emailTemplate.getToList());

        if ( toList == null || toList.toString().length() < 1) {
            resultBean.setError("emailTemplate", "Email 'to' receivers cannot be empty");
        }
    }

    public RuleResultBean validateDeliveryStatusChange( EmailContent emailContent, Status toStatus ) {
        RuleResultBean resultBean = new RuleResultBean();
        if (!toStatus.equals(Status.APPROVED)) {
            resultBean.setError("deliveryStatus", "Email content is not yet approved.");
        }
        return resultBean;
    }

    public RuleResultBean validateLifeStatusChange(EmailContent emailContent, Status toStatus) {

        RuleResultBean resultBean = new RuleResultBean();

        if (toStatus.equals(Status.DRAFT)) {
            return resultBean;
        }

        if (toStatus.equals(Status.PENDING)) {
            resultBean.setError("deliveryStatus", "Changing status of pending email is not allowed");
            return resultBean;
        }

        if (emailContent.getDeliveryStatus().equals(Status.COMPLETED)) {
            resultBean.setError("deliveryStatus", "Status of sent email is not allowed.");
            return resultBean;
        }

        Status lifeStatus = emailContent.getLifeStatus();
        Status delStatus = emailContent.getDeliveryStatus();

        if (toStatus.equals(Status.APPROVED)) {
            validateTemplate(emailContent.getTemplate(), resultBean);
        }

        return resultBean;
    }

    public RuleResultBean validateForSend(EmailContent emailContent) {

        RuleResultBean bean = new RuleResultBean();

        validateTemplate(emailContent.getTemplate(), bean);
        validateEmpty(bean, emailContent.getTemplate().getFrom(), "from", "Email sender cannot be null");
        validateCollection(bean, emailContent.getReceivers(), "receivers", "There has to be at least 1 receiver");

        return bean;
    }

}
