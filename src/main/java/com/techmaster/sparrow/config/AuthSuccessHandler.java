package com.techmaster.sparrow.config;

import com.techmaster.sparrow.entities.misc.UserLogin;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.services.apis.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired private UserLoginService userLoginService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null && authentication.getName() != null) {
            String userName = authentication.getName();
            UserLogin userLogin = userLoginService.createFromRequest(request, Status.SUCCESS, userName);
            userLogin.setUserName(userName != null ? userName : userLogin.getUserName());
            userLoginService.save(userLogin);
        }
    }

}
