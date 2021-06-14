package pgd.irbl.business.dao;

import org.apache.ibatis.annotations.Mapper;
import pgd.irbl.business.po.RepoCommit;

import java.util.List;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-09 11:08
 */
@Mapper
public interface RepoCommitMapper {

    String findGitUrlByCommitId(String commit);
    String findHoleCommitId(String commit);
    int insertRepoCommit(RepoCommit repoCommit);
    int insertRepoCommitByList(List<RepoCommit> repoCommitList);
    List<String> getAllCommitIdByGitUrl(String gitUrl);
}
