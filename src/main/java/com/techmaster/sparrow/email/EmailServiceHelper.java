package com.techmaster.sparrow.email;

import com.techmaster.sparrow.entities.email.EmailTemplate;
import com.techmaster.sparrow.enums.EmailReasonType;
import com.techmaster.sparrow.repositories.EmailTemplateRepo;
import com.techmaster.sparrow.repositories.SparrowBeanContext;

public class EmailServiceHelper {

    public static EmailTemplate getTemplate(EmailReasonType reasonType) {

        EmailTemplateRepo repository = SparrowBeanContext.getBean(EmailTemplateRepo.class);
        EmailTemplate emailTemplate = null;

        return emailTemplate;
    }

}
