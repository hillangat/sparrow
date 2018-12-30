package com.techmaster.sparrow.services.apis;

import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;

public interface EmailContentService {

    ResponseData search(GridDataQueryReq queryReq);
    EmailContent getById( long emailContentId );
    RuleResultBean save( EmailContent emailContent);
    RuleResultBean edit( EmailContent emailContent );
    RuleResultBean deleteEmailContent( long emailContentId );
    RuleResultBean setLifeStatus(long emailContentId, Status status);
    RuleResultBean setDeliveryStatus(long emailContentId, Status status);
    RuleResultBean send( long emailContentId );

}
