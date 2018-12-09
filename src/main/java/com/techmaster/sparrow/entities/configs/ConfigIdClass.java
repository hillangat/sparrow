package com.techmaster.sparrow.entities.configs;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class ConfigIdClass implements Serializable {
    private String code;
    private String context;
}
