package com.techmaster.sparrow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.services.apis.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthFailedHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private String failedURL = "/login?error";


    @Autowired
    private UserLoginService userLoginService;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", Calendar.getInstance().getTime());
        data.put("exception", exception.getMessage());

        response.getOutputStream()
                .println(objectMapper.writeValueAsString(data));


        userLoginService.createFromRequest(request, Status.FAILED, null);

        redirectStrategy.sendRedirect(request, response, failedURL);
    }
}
