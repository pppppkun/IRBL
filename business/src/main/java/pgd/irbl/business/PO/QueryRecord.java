package pgd.irbl.business.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bson.Document;
import org.springframework.data.annotation.Id;
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
    @Id
    @ApiModelProperty("id标记了每一个唯一的queryRecord")
    String id;
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

    public QueryRecord() {
    }

    public QueryRecord(Document document) {
        id = document.getObjectId("_id").toString();
        repoCommitId = document.getString("repoCommitId");
        userId = ((Number) document.get("userId")).longValue();
        gitUrl = document.getString("gitUrl");
        fileScoreList = document.getList("fileScoreList", FileScore.class);
        queryTime = new Timestamp(document.getLong("queryTime"));
        queryRecordState = QueryRecordState.valueOf(document.getString("queryRecordState"));
    }

}
