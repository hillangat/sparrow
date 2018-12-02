package com.techmaster.sparrow.rules;

import com.techmaster.sparrow.entities.User;
import com.techmaster.sparrow.util.SparrowUtil;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;

import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class KnowledgeBaseHelper {

    public static void main(String[] args) {
        KnowledgeBaseHelper helper = new KnowledgeBaseHelper();
        helper.executeUserDrools();
    }

    public void executeUserDrools() {

        try {
            KnowledgeBase kbase = readKnowledgeBase();
            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

            User user = SparrowUtil.addAuditInfo(new User(), "admin");
            user.setUserName(null);
            user.setLastName("Langat");
            user.setNickName("Kip");
            user.setEmail("hillangat@gmail.com");
            user.setUserId(1);

            ksession.insert(user);

            ksession.fireAllRules();

            System.out.println(user);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static KnowledgeBase readKnowledgeBase() throws Exception {

        KnowledgeBuilder kbuilder =
                KnowledgeBuilderFactory.newKnowledgeBuilder();

        kbuilder.add(ResourceFactory.newClassPathResource("\\rules\\User.drl"),
                ResourceType.DRL);

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

}
