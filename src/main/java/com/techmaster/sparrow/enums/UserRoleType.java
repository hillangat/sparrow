package com.techmaster.sparrow.enums;

public enum  UserRoleType {

    ADMIN("ADMIN","Admin"),
    USER("USER","User"),
    EXT_APP("EXT_APP","External Application"),
    SONG_MANAGER( "SONG_MANAGER", "Song Manager" ),
    EML_TMPLT_ROLE("TMPLT_ROLE_USER","Email Template Manager");

    private final String name;
    private final String desc;


    private UserRoleType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }

}
