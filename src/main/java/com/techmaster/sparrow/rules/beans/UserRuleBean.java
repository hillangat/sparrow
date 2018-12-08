package com.techmaster.sparrow.rules.beans;

import com.techmaster.sparrow.entities.User;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import lombok.*;

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
