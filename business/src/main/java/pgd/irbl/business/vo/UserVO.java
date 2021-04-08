package pgd.irbl.business.vo;

import lombok.Data;
import pgd.irbl.business.enums.UserRole;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-05 22:55
 */
@Data
public class UserVO {
    String username;
    UserRole role;
    Integer queryNum;
    String token;
}
