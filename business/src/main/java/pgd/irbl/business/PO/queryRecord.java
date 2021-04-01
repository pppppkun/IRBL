package pgd.irbl.business.PO;

import lombok.Data;
import pgd.irbl.business.VO.FileScore;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-01 15:57
 */
@Data
public class queryRecord {
    Long id;
    String repoCommitId;
    String gitUrl;
    List<FileScore> fileScoreList;
    Timestamp queryTime;
}
