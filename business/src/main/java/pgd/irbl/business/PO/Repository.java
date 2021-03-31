package pgd.irbl.business.PO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-25 14:10
 */
@Data
public class Repository {
    Long id;
    String gitUrl;
    @ApiModelProperty(value = "对项目的描述")
    String description;
    @ApiModelProperty(value = "项目的状态，目前只有Dev和Abandon")
    String state;
    @ApiModelProperty(value = "这个项目被查询的次数")
    Long queryNum;
    @ApiModelProperty(value = "注册时间")
    Date startTime;
    @ApiModelProperty(value = "如果仓库被注销，那么这个属性就会被赋值")
    Date endTime;
}
