package pgd.irbl.business.Controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pgd.irbl.business.VO.FileScore;
import pgd.irbl.business.VO.LoginVO;
import pgd.irbl.business.VO.RegisterVO;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-29 13:39
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    @ApiOperation(value = "登陆")
    public ResponseVO login(@RequestBody LoginVO loginVO){
        return ResponseVO.buildSuccess();
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册")
    public ResponseVO register(@RequestBody RegisterVO registerVO){
        return ResponseVO.buildSuccess();
    }



}
