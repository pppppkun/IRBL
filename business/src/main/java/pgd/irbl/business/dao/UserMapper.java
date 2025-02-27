package pgd.irbl.business.dao;

import org.apache.ibatis.annotations.Mapper;
import pgd.irbl.business.po.User;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-30 15:30
 */
@Mapper
public interface UserMapper {

    int register(User user);
    User login(String username);
    Long findUserIdByUsername(String username);
}
