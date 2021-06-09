package pgd.irbl.business.dao;

import org.apache.ibatis.annotations.Mapper;
import pgd.irbl.business.po.RepoCommit;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-09 11:08
 */
@Mapper
public interface RepoCommitMapper {

    String findGitUrlByCommitId(String commit);
    int insertRepoCommit(RepoCommit repoCommit);

}
