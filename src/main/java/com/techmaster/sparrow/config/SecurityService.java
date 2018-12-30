package com.techmaster.sparrow.config;

public interface SecurityService {

    String findLoggedInUsername();
    void autologin(String username, String password);
    void logout();

}
