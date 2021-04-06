package pgd.irbl.business.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import pgd.irbl.business.VO.FileScore;
import pgd.irbl.business.enums.QueryRecordState;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-01 15:57
 */
@Data
@ApiModel("每次查询返回的对象")
public class QueryRecord {
    @ApiModelProperty("id标记了每一个唯一的queryRecord")
    Long id;
    @ApiModelProperty("repoCommitId是你查询的时候输入的commitId，如果查询的项目没有注册，或者注册的项目不是Dev状态，这个字段会被设置成\"未设置commitId\"")
    String repoCommitId;
    Long userId;
    @ApiModelProperty("只要不是已经注册了的项目或者注册了但不是Dev状态，这里都是\"未设置gitUrl\"")
    String gitUrl;
    @ApiModelProperty("刚刚查询的时候，这个值是null，等待查询结果完毕，才会被设置成正确结果")
    List<FileScore> fileScoreList;
    @ApiModelProperty("查询的时间，返回的时候是字符串")
    Timestamp queryTime;
    @ApiModelProperty("queryRecord的状态，目前有三种")
    QueryRecordState queryRecordState;

}
