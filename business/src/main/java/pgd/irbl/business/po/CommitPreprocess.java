package pgd.irbl.business.po;

import lombok.Data;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-27 10:03
 */
@Data
public class CommitPreprocess {
    Long id;
    String repoCommitId;
    String processCommitId;
    String filePath;
}
