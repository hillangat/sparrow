package com.techmaster.sparrow.services;

import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;

public class EmailContentServiceImpl implements EmailContentService {

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
        return null;
    }
}
