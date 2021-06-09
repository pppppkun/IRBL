package pgd.irbl.business.po;

import lombok.Data;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-09 11:32
 */
@Data
public class RepoCommit {

    Long id;
    String commit;
    String gitUrl;

}
