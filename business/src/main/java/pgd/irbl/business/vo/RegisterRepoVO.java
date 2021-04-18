package pgd.irbl.business.VO;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-24 21:19
 */
@Data
@ApiModel(description = "注册项目所需要的VO")
public class RegisterRepoVO {
    String gitUrl;
    String description;
}
