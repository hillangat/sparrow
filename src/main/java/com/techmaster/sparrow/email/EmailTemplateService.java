package com.techmaster.sparrow.email;

import com.techmaster.sparrow.entities.email.EmailAttachment;
import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.entities.email.EmailTemplate;
import com.techmaster.sparrow.enums.EmailReasonType;
import lombok.*;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateService {

    private EmailTemplate emailTemplate;
    private List<EmailReceiver> emailReceivers;
    private List<EmailAttachment> attachments;
    private String greeter;
    private String contentBody;
    private String signature;
    private String securityStatement;
    private String readyBody;
    private EmailReasonType reasonType;
    private String subject;

    public EmailTemplateService (EmailReasonType reasonType, List<EmailReceiver> emailReceivers, String subject) {
        this.reasonType = reasonType;
        this.emailReceivers = emailReceivers;
        this.subject = subject;
    }

}
