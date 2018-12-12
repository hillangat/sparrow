package com.techmaster.sparrow.rules.beans;

import com.techmaster.sparrow.entities.misc.User;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UserRuleBean extends User {

    RuleResultBean ruleResultBean = new RuleResultBean();

    public boolean isSuccess() {
        return ruleResultBean.isSuccess();
    };
}
