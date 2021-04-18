package pgd.irbl.business.service;

import pgd.irbl.business.vo.LoginRegisterVO;
import pgd.irbl.business.vo.ResponseVO;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-30 15:31
 */
public interface UserService {
    ResponseVO login(LoginRegisterVO loginVO);
    ResponseVO register(LoginRegisterVO registerVO);
}
