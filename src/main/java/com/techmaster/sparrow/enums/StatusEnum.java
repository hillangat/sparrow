package com.techmaster.sparrow.enums;

public enum StatusEnum {

    FAILED("failed", "Failed processing"),
    SUCCESS("success", "Successful processing"),
    PENDING("pending", "Pending processing");

    private String status;
    private String description;

    StatusEnum(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

}