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
import pgd.irbl.business.enums.RepoState;
import pgd.irbl.business.po.RepoCommit;
import pgd.irbl.business.po.Repository;

import java.sql.Date;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-09 11:22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepoCommitMapperTest {

    @Autowired
    RepoCommitMapper repoCommitMapper;

    @Test
    public void Test1insert(){
        RepoCommit repoCommit = new RepoCommit();
        repoCommit.setGitUrl("https://github.com/pppppkun/TuringMachine.git");
        repoCommit.setCommit("0517be452016737b942e0fe337c87ccde97f3f21");
        int i = repoCommitMapper.insertRepoCommit(repoCommit);
        Assert.assertEquals(1, i);
    }

    @Test
    public void Test2find(){
        String gitUrl = repoCommitMapper.findGitUrlByCommitId("0517be45");
        Assert.assertEquals("https://github.com/pppppkun/TuringMachine.git", gitUrl);
    }

}
