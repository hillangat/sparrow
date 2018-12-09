package com.techmaster.sparrow.rules.abstracts;

import lombok.Data;
import org.drools.builder.ResourceType;

@Data
public class RuleTypeBean {

    private String name;
    private int index;
    private String resource;
    private String resourceType;

    public ResourceType getType () {
        switch (resourceType) {
            case "DRL": return ResourceType.DRL;
            case "XDRL": return ResourceType.XDRL;
            case "DSL": return ResourceType.DSL;
            case "DSLR": return ResourceType.DSLR;
            case "DRF": return ResourceType.DRF;
            case "BPMN2": return ResourceType.BPMN2;
            case "DTABLE": return ResourceType.DTABLE;
            case "PKG": return ResourceType.PKG;
            case "BRL": return ResourceType.BRL;
            case "CHANGE_SET": return ResourceType.CHANGE_SET;
            case "XSD": return ResourceType.XSD;
            case "PMML": return ResourceType.PMML;
            case "DESCR": return ResourceType.DESCR;
            default: throw new IllegalArgumentException("Invalid type passed!!!");
        }
    }

}
