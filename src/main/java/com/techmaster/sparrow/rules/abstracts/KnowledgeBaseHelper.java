package com.techmaster.sparrow.rules.abstracts;

import org.drools.builder.ResourceType;

import java.util.List;
import java.util.Map;

public interface KnowledgeBaseHelper<T, I> {
    List<T> runRules(List<I> objects);
}
