package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.entities.misc.User;
import com.techmaster.sparrow.repositories.UserRepository;
import com.techmaster.sparrow.util.SparrowUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository;

    @Before
    public void setup() {
        userController = new UserController();
        userRepository = mock(UserRepository.class);
        Whitebox.setInternalState(userController, "userRepository", userRepository);
        when(userRepository.save(any())).thenAnswer(m -> m.getArguments()[0]);
    }

    @Test
    public void testSaveUser() {
        User user = SparrowUtil.addAuditInfo(new User(), "admin");
        user.setFirstName("Hillary");
        user.setLastName("Langat");
        user.setEmail("hillangat@gmail.comss");
        ResponseEntity<ResponseData> data = userController.saveOrUpdate(user);
        System.out.println("data :" +  data.toString());
        Assert.assertNotNull(data);
    }
}
