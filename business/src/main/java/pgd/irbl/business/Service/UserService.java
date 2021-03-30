package pgd.irbl.business.Service;

import pgd.irbl.business.VO.LoginRegisterVO;
import pgd.irbl.business.VO.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-30 15:31
 */
public interface UserService {
    ResponseVO login(LoginRegisterVO loginVO);
    ResponseVO register(LoginRegisterVO registerVO);
}
