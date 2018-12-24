package com.techmaster.sparrow.enums;

public enum UserDisableReasonType {

    USER_ROLES( "User has no more roles in the system" ),
    REGULATIONS_VIOLATIONS( "User violates government regulations" ),
    UNVERIFIED_AUTHENTICATION( "User authentication has not been verified." ),
    AUTHENTICATION_FAILURE( "Too many authentication trial failures." );

    UserDisableReasonType( String description ) {
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return this.description;
    }

}
