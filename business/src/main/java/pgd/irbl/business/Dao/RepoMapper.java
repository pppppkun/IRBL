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
    int deleteRepo(int repoId);
    int insertRepo(Repository repository);

}
