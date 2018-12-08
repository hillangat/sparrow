package com.techmaster.sparrow.email;

import com.techmaster.sparrow.enums.EmailReasonType;

public interface SparrowEmailService {

    void sendEmail(EmailReasonType reasonType, String email);

}
