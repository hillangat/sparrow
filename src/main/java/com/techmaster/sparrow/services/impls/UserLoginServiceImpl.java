package com.techmaster.sparrow.services.impls;

import com.techmaster.sparrow.config.ApplicationProperties;
import com.techmaster.sparrow.entities.misc.UserLogin;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.repositories.UserLoginRepo;
import com.techmaster.sparrow.services.apis.UserLoginService;
import com.techmaster.sparrow.services.apis.UserService;
import com.techmaster.sparrow.util.SparrowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired private UserLoginRepo userLoginRepo;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private ApplicationProperties applicationProperties;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void encrypt(UserLogin userLogin) {
        userLogin.setPassword(bCryptPasswordEncoder.encode(userLogin.getPassword()));
    }

    @Override
    public List<UserLogin> getLatest(String userName, String status) {
        int count = applicationProperties.getUserMaxFailedCount();
        List<UserLogin> userLogins = userLoginRepo.getLatestLogins(userName, status, count);
        return userLogins;
    }

    @Override
    public void save(UserLogin userLogin) {
        try {
            userLoginRepo.save(userLogin);
        } catch (Exception e) {
            SparrowUtil.logException(logger, e, "Application error occurred while trying to save user login information.");
        }
    }

    @Override
    public boolean isBlocked(String userName) {
        int count = getLatestFailedCount(userName);
        return count > applicationProperties.getUserMaxFailedCount();
    }

    @Override
    public int getLatestFailedCount(String userName) {
        int count = applicationProperties.getUserMaxFailedCount();
        List<UserLogin> userLogins = userLoginRepo.getLatestLogins(userName, Status.FAILED.toString(), count);
        return userLogins != null ? userLogins.size() : 0;
    }

    @Override
    public UserLogin createFromRequest(HttpServletRequest request, Status status, String userName) {

        userName = userName == null ? request.getParameter("j_username") : userName;
        userName = userName == null ? request.getParameter("username") : userName;

        String password = request.getParameter("j_password");
        password = password == null ? request.getParameter("password") : password;

        String IpAddress = request.getRemoteAddr();

        password = password != null ? bCryptPasswordEncoder.encode(password) : null;

        if (userName != null) {
            String admin = applicationProperties.getAdminUserName();
            UserLogin userLogin = SparrowUtil.addAuditInfo(new UserLogin(), admin);
            userLogin.setUserName(userName);
            userLogin.setPassword(password);
            userLogin.setIpAddress(IpAddress);
            userLogin.setStatus(status);
            save(userLogin);
            return userLogin;
        }

        return null;
    }
}
