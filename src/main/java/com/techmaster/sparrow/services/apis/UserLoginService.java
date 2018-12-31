package com.techmaster.sparrow.services.apis;

import com.techmaster.sparrow.entities.misc.UserLogin;
import com.techmaster.sparrow.enums.Status;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserLoginService {

    void encrypt(UserLogin userLogin);
    List<UserLogin> getLatest(String userName, String status);
    void save( UserLogin userLogin );
    boolean isBlocked( String userName );
    int getLatestFailedCount( String userName );
    UserLogin createFromRequest(HttpServletRequest request, Status status, String userName);

}
