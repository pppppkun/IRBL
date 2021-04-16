package pgd.irbl.business.Dao;

import org.apache.ibatis.annotations.Mapper;
import pgd.irbl.business.PO.Repository;

import java.util.List;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-25 23:15
 */
@Mapper
public interface RepoMapper {

    List<Repository> selectAllRepo();
    int deleteRepo(Long repoId);
    int insertRepo(Repository repository);
    Long findRepoIdByGitUrl(String gitUrl);
    int updateDescription(Long repoId, String description);
    Repository findRepoById(Long repoId);

}
