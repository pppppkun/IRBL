package pgd.irbl.business.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-24 21:19
 */
@Data
@ApiModel(description = "注册项目所需要的VO")
public class RegisterRepoVO {
    private String gitUrl;
}
