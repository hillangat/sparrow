package com.techmaster.sparrow.enums;

public enum Status {

    FAILED("failed", "Failed processing"),
    SUCCESS("success", "Successful processing"),
    PENDING("pending", "Pending processing"),
    DRAFT("draft", "Draft content"),
    CONCEPTUAL("conceptual", "Just created"),
    REVIEW("review", "Review"),
    APPROVED("approved", "Approved"),
    INVALID("invalid", "Failed validation"),
    READY("ready", "Ready"),
    REJECTED("rejected", "Rejected");

    private String status;
    private String description;

    Status(String status, String description) {
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
