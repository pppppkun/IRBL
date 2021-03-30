package pgd.irbl.business.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pgd.irbl.business.Dao.UserMapper;
import pgd.irbl.business.PO.User;
import pgd.irbl.business.Service.UserService;
import pgd.irbl.business.Utils.MD5Encryption;
import pgd.irbl.business.VO.LoginRegisterVO;
import pgd.irbl.business.VO.ResponseVO;
import pgd.irbl.business.enums.UserRole;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-30 15:45
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    public static final String REGISTER_ERROR="注册失败";
    public static final String USER_EXISTS="该用户名已被使用";
    public static final String USER_NO_EXISTS="该用户不存在";
    public static final String PASSWORD_ERROR="密码错误";

    UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public ResponseVO login(LoginRegisterVO loginVO){
//        if(MD5Encryption.encrypt(loginVO.getPassword()).equals(userMapper.login(loginVO.getUsername()))) return ResponseVO.buildSuccess();
        if(userMapper.findUserByUsername(loginVO.getUsername())==null) return ResponseVO.buildFailure(USER_NO_EXISTS);

        if(MD5Encryption.encrypt(loginVO.getPassword()).equals(userMapper.login(loginVO.getUsername()))) return ResponseVO.buildSuccess();
        else return ResponseVO.buildFailure(PASSWORD_ERROR);

    }

    @Override
    public ResponseVO register(LoginRegisterVO registerVO) {
        if(userMapper.findUserByUsername(registerVO.getUsername())!=null) return ResponseVO.buildFailure(USER_EXISTS);
        User user = new User();
        user.setUsername(registerVO.getUsername());
        user.setPassword(MD5Encryption.encrypt(registerVO.getPassword()));
        user.setRole(UserRole.Developer);
        user.setQueryNum(0);
        int ret = userMapper.register(user);
        if(ret == 1) return ResponseVO.buildSuccess();
        else{
            log.warn(REGISTER_ERROR, user);
            return ResponseVO.buildFailure(REGISTER_ERROR);
        }
    }
}
