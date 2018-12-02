package com.techmaster.sparrow.rules.abstracts;

import com.techmaster.sparrow.util.SparrowUtil;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractKnowledgeBaseHelper<T, I> implements KnowledgeBaseHelper<T, I> {

    protected RuleExceptionType exceptionType;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract Map<String, ResourceType> getRules();

    /**
     *
     * @param resources - Map of url to resource and the type it is.
     * @return
     * @throws Exception
     */
    protected StatefulKnowledgeSession readKnowledgeBase(Map<String, ResourceType> resources) throws Exception {

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        resources.entrySet().forEach(r -> {
            kbuilder.add(ResourceFactory.newClassPathResource(r.getKey()), r.getValue());
        });

        KnowledgeBuilderErrors errors = kbuilder.getErrors();

        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase.newStatefulKnowledgeSession();
    }

    protected <T> RuleExceptionType fireRules (Map<String, ResourceType> rules, List<T> ruleBeans) {
        try {
            StatefulKnowledgeSession kSession = readKnowledgeBase(getRules());
            ruleBeans.forEach(r -> kSession.insert(r));
            kSession.fireAllRules();
            return null;
        } catch (Exception e) {
            logger.error("Application error occurred while trying to execute rules: " +
                    SparrowUtil.stringifySet(rules.entrySet().stream().map(r -> r.getKey()).collect(Collectors.toSet())));
            e.printStackTrace();
            return RuleExceptionType.APPLICATION;
        }
    }

}
