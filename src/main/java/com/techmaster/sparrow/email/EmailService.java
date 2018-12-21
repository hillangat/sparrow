package com.techmaster.sparrow.email;

import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;

public interface EmailService {

    RuleResultBean send(EmailContent emailContent);

}
