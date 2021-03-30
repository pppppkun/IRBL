package pgd.irbl.business.Controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pgd.irbl.business.Service.UserService;
import pgd.irbl.business.VO.LoginRegisterVO;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-29 13:39
 */
@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "登陆")
    public ResponseVO login(@RequestBody LoginRegisterVO loginVO){
        return userService.login(loginVO);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册")
    public ResponseVO register(@RequestBody LoginRegisterVO registerVO){
        return userService.register(registerVO);
    }



}
