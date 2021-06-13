package pgd.irbl.business.vo;

import lombok.Data;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-13 11:46
 */
@Data
public class SimpleCommitMessageVO {
    String commitId;
    String message;
    String time;
}
