package pgd.irbl.business.dao;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pgd.irbl.business.po.Repository;
import pgd.irbl.business.enums.RepoState;

import java.sql.Date;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-27 14:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepoMapperTest {

    @Autowired
    RepoMapper repoMapper;
    /**
     *     List<Repository> selectAllRepo();
     *     int deleteRepo(Long repoId);
     *     int insertRepo(Repository repository);
     *     Long findRepoIdByGitUrl(String gitUrl);
     *     Long findRepoIdByRepoId(Long repoId);
     *     int updateDescription(Long repoId, String description);
     */
    @Test
    public void Test1selectAllRepo(){
        Assert.assertEquals(3, repoMapper.selectAllRepo().size());
    }

    @Test
    public void Test2deleteRepo(){
        int ret = repoMapper.deleteRepo(1L);
        Assert.assertEquals(1, ret);
        Assert.assertEquals(2, repoMapper.selectAllRepo().size());
    }

    @Test
    public void Test3insertRepo(){
        Repository repository = new Repository();
        repository.setDescription("Test");
        repository.setGitUrl("xxxx.xxxx.xxxx");
        repository.setQueryNum(0);
        repository.setStartTime(new Date(System.currentTimeMillis()));
        repository.setState(RepoState.Dev);
        int ret = repoMapper.insertRepo(repository);
        Assert.assertEquals(1, ret);
        Assert.assertEquals(4L, repository.getId().longValue());
        Assert.assertEquals(3, repoMapper.selectAllRepo().size());
    }

    @Test
    public void Test4findRepoIdByGitUrl(){
        Assert.assertEquals(2, repoMapper.findRepoIdByGitUrl("ssh://git@212.129.149.40:222/pgd/frontend-irbl.git").longValue());
    }

    @Test
    public void Test5updateDescription(){
        repoMapper.updateDescription(2L, "AfterUpdate");
        Assert.assertEquals("AfterUpdate", repoMapper.findRepoById(2L).getDescription());
    }

}
