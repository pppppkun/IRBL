package pgd.irbl.business.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pgd.irbl.business.Dao.UserMapper;
import pgd.irbl.business.PO.User;
import pgd.irbl.business.Service.UserService;
import pgd.irbl.business.Utils.JwtUtil;
import pgd.irbl.business.Utils.MD5Encryption;
import pgd.irbl.business.VO.LoginRegisterVO;
import pgd.irbl.business.VO.ResponseVO;
import pgd.irbl.business.VO.UserVO;
import pgd.irbl.business.enums.UserRole;
import static pgd.irbl.business.Constant.UserConstant.*;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-30 15:45
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {


    UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public ResponseVO login(LoginRegisterVO loginVO){
//        if(MD5Encryption.encrypt(loginVO.getPassword()).equals(userMapper.login(loginVO.getUsername()))) return ResponseVO.buildSuccess();
        if(userMapper.findUserIdByUsername(loginVO.getUsername())==null) return ResponseVO.buildFailure(USER_NO_EXISTS);
        User user = userMapper.login(loginVO.getUsername());
        if(MD5Encryption.encrypt(loginVO.getPassword()).equals(user.getPassword())) {
            UserVO userVO = new UserVO();
            userVO.setUsername(user.getUsername());
            userVO.setRole(user.getRole());
            userVO.setQueryNum(user.getQueryNum());
            userVO.setToken(JwtUtil.createToken(user.getId(), tokenTime));
            return ResponseVO.buildSuccess(userVO);
        }
        else return ResponseVO.buildFailure(PASSWORD_ERROR);
    }

    @Override
    public ResponseVO register(LoginRegisterVO registerVO) {
        if(userMapper.findUserIdByUsername(registerVO.getUsername())!=null) return ResponseVO.buildFailure(USER_EXISTS);
        User user = new User();
        user.setUsername(registerVO.getUsername());
        user.setPassword(MD5Encryption.encrypt(registerVO.getPassword()));
        user.setRole(UserRole.Developer);
        user.setQueryNum(0);
        int ret = userMapper.register(user);
        if(ret == 1) return ResponseVO.buildSuccess(REGISTER_OK);
        else return ResponseVO.buildFailure(REGISTER_ERROR);
    }
}
