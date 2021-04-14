package pgd.irbl.business.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pgd.irbl.business.VO.LoginRegisterVO;
import static pgd.irbl.business.constant.UserConstant.*;
/**
 * @Author: pkun
 * @CreateTime: 2021-03-30 18:34
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {
    @Autowired
    UserService userService;

    LoginRegisterVO loginRegisterVO;

    @Before
    public void setUp(){
        loginRegisterVO = new LoginRegisterVO();
        loginRegisterVO.setUsername("develop");
        loginRegisterVO.setPassword("1234567");
    }

    @Test
    public void Test1Register(){
        userService.register(loginRegisterVO);
        Assert.assertTrue(userService.login(loginRegisterVO).getSuccess());
        Assert.assertEquals(USER_EXISTS ,userService.register(loginRegisterVO).getContent());
    }

    @Test
    public void Test2Login(){
        Assert.assertTrue(userService.login(loginRegisterVO).getSuccess());
    }

    @Test
    public void Test3Login(){
        loginRegisterVO.setPassword("lichun");
        Assert.assertFalse(userService.login(loginRegisterVO).getSuccess());
    }

    @Test
    public void Test4Login(){
        loginRegisterVO.setUsername("develop1");
        Assert.assertFalse(userService.login(loginRegisterVO).getSuccess());
    }

}
