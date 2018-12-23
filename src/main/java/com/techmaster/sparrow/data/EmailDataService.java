package com.techmaster.sparrow.data;

import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.enums.EmailReceiverType;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.repositories.SparrowJDBCExecutor;
import com.techmaster.sparrow.util.SparrowUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmailDataService {

    public static List<EmailReceiver> createEmailReceivers(List<Long> userIds, Long contentId) {

        List<Map<String, Object>> receiverDetails = getReceiverDetails(userIds);
        List<EmailReceiver> emailReceivers = new ArrayList<>();

        for( Map<String, Object> detail : receiverDetails ) {
            EmailReceiver receiver = SparrowUtil.addAuditInfo(new EmailReceiver(), "admin");
            receiver.setContentId(contentId);
            receiver.setEmail(detail.get("EML").toString());
            receiver.setFirstName(detail.get("FRST_NAM").toString());
            receiver.setLastName(detail.get("LST_NAM").toString());
            receiver.setReceiverId(0);
            receiver.setMiddleName(null);
            receiver.setReceiverType(EmailReceiverType.TO);
            emailReceivers.add(receiver);
        }

        return emailReceivers;
    }

    public static List<Map<String, Object>> getReceiverDetails(List<Long> receiverIds) {
        String query = SparrowUtil.getQueryForSqlId("getEmailReceiverDetails");
        SparrowJDBCExecutor executor = SparrowBeanContext.getBean(SparrowJDBCExecutor.class);
        List<Object> params = receiverIds.stream().map( a -> (Object)a)
                .collect(Collectors.toList());
        List<Map<String, Object>> rowMaps = executor.executeQueryRowMap(query, params);
        return rowMaps;
    }



}
