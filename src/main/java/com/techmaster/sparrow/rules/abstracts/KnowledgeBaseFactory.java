package com.techmaster.sparrow.rules.abstracts;

import com.techmaster.sparrow.enums.RuleTypes;
import com.techmaster.sparrow.rules.beans.UserKnowledgeBaseHelper;

public class KnowledgeBaseFactory {

    public static KnowledgeBaseHelper getHelper(RuleTypes type) {
        switch (type) {
            case USER: return new UserKnowledgeBaseHelper();
            default: return null;
        }
    }

}
