package com.techmaster.sparrow.email;

import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.enums.EmailReasonType;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;

import java.util.List;

public interface EmailService {

    RuleResultBean send(EmailContent emailContent);

}
