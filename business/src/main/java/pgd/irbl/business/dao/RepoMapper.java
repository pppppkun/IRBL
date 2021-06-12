package pgd.irbl.business.dao;

import org.apache.ibatis.annotations.Mapper;
import pgd.irbl.business.po.Repository;

import java.util.List;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-25 23:15
 */
@Mapper
public interface RepoMapper {

    List<Repository> selectAllRepo();
    int deleteRepo(Long repoId);
    String findGitUrlByRepoId(Long repoId);
    int insertRepo(Repository repository);
    Long findRepoIdByGitUrl(String gitUrl);
    int updateDescription(Long repoId, String description);
    Repository findRepoById(Long repoId);
    int updateQueryNum(String gitUrl);
    int findQueryNumByGitUrl(String gitUrl);

}
