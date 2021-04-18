package pgd.irbl.business.PO;

import lombok.Data;
import pgd.irbl.business.enums.UserRole;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-27 09:54
 */
@Data
public class User {
    Long id;
    String username;
    String password;
    UserRole role;
    Integer queryNum;
}
