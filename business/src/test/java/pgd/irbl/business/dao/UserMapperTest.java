package pgd.irbl.business.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pgd.irbl.business.po.User;
import pgd.irbl.business.enums.UserRole;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-30 17:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;
    User admin;
    User dev;

    @Value("${token}")
    String token;

    @Before
    public void setUp(){
        System.out.println();
        admin = new User();
        dev = new User();
        admin.setRole(UserRole.Admin);
        admin.setId(1L);
        admin.setPassword("password");
        admin.setUsername("admin");
        admin.setQueryNum(1);
        dev.setUsername("developer");
        dev.setRole(UserRole.Developer);
        dev.setPassword("password1");
        dev.setQueryNum(123);
    }

    @Test
    public void Test1Register(){
        int ret = userMapper.register(dev);
        Assert.assertEquals(1, ret);
    }

    @Test
    public void Test2Login(){
        User user = userMapper.login(dev.getUsername());
        Assert.assertEquals(dev.getPassword(), user.getPassword());
        User user1 = userMapper.login("Test");
        Assert.assertNull(user1);
    }

    @Test
    public void Test3FindUserByUsername(){
        Long id = userMapper.findUserIdByUsername("admin");
        Assert.assertEquals(admin.getId(), id);
    }
//    int register(User user);
//    String login(String username);
//    Long findUserByUsername(String username);
}
