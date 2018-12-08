package com.techmaster.sparrow.rules.beans;

import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import lombok.Data;

@Data
public class DefaultRuleBean {

    RuleResultBean ruleResultBean = new RuleResultBean();

    public boolean isSuccess() {
        return ruleResultBean.getErrors().isEmpty() &&
                ruleResultBean.getStatus().equals(StatusEnum.SUCCESS);
    };
}
