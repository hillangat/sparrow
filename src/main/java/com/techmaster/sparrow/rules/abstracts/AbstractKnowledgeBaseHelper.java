package com.techmaster.sparrow.rules.abstracts;

import com.techmaster.sparrow.util.SparrowUtil;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractKnowledgeBaseHelper<T, I> implements KnowledgeBaseHelper<T, I> {

    protected RuleExceptionType exceptionType;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract List<RuleTypeBean> getRules();

    /**
     *
     * @param ruleTypeBeans - Map of url to resource and the type it is.
     * @return
     * @throws Exception
     */
    protected KnowledgeBase readKnowledgeBase(List<RuleTypeBean> ruleTypeBeans) throws URISyntaxException {

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        ruleTypeBeans.forEach(r -> {
            String path = this.getClass().getClassLoader().getResource("rules/files/user_create.drl").getFile();
            kbuilder.add(ResourceFactory.newFileResource(new File(path)), r.getType());
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
        return kbase;
    }

    protected <T> RuleExceptionType fireRules (List<RuleTypeBean> ruleTypeBeans, List<T> ruleBeans) {
        try {

            KnowledgeBase kbase = readKnowledgeBase(ruleTypeBeans);
            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
            ruleBeans.forEach(r -> ksession.insert(r));
            ksession.fireAllRules();
            ksession.dispose();

            return null;
        } catch (Exception e) {

            Set<String> stringSet = ruleTypeBeans
                    .stream().map(r -> r.getResource())
                    .collect(Collectors.toSet());

            String str = SparrowUtil.stringifySet(stringSet);

            logger.error("Application error occurred while trying to execute rules: " + str );

            e.printStackTrace();
            return RuleExceptionType.APPLICATION;
        }
    }

}
