package com.techmaster.sparrow.rules.beans;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.entities.User;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.rules.abstracts.AbstractKnowledgeBaseHelper;
import com.techmaster.sparrow.rules.abstracts.RuleExceptionType;
import com.techmaster.sparrow.rules.abstracts.RuleTypeBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.drools.builder.ResourceType;
import org.drools.runtime.StatefulKnowledgeSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserKnowledgeBaseHelper extends AbstractKnowledgeBaseHelper<UserRuleBean, User> {

    @Override
    public List<UserRuleBean> runRules(List<User> objects) {

        List<UserRuleBean> ruleBeans = new ArrayList<>();

        objects.forEach(o -> {
            UserRuleBean ruleBean = SparrowUtil.clone(new UserRuleBean(), o);
            ruleBeans.add(ruleBean);
        });

        RuleExceptionType exceptionType = fireRules(getRules(), ruleBeans);

        if (exceptionType != null) {
            ruleBeans.forEach(r -> r.getRuleResultBean().setApplicationError());
        }

        return ruleBeans;
    }


    public List<RuleTypeBean> getRules() {
        List<RuleTypeBean> types = SparrowCacheUtil.getInstance().getRuleTypeBeans("user.create");
        return types;
    }
}
