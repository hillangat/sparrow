package com.techmaster.sparrow.entities.configs;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigIdClass implements Serializable {
    private String code;
    private String context;
}
