package pgd.irbl.business.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pgd.irbl.business.po.Repository;
import pgd.irbl.business.vo.DeleteRepoVO;
import pgd.irbl.business.vo.ModifyRepoVO;
import pgd.irbl.business.vo.RegisterRepoVO;
import pgd.irbl.business.enums.RepoState;
import pgd.irbl.business.vo.ResponseVO;

import static pgd.irbl.business.constant.ManageConstant.*;

import java.sql.Date;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-01 18:25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManageServiceTest {

    @Autowired
    ManageService manageService;
    Repository repository;
    /**
     *     ResponseVO registerRepo(RegisterRepoVO registerRepoVO);
     *     ResponseVO deleteRepo(DeleteRepoVO deleteRepoVO);
     *     List<Repository> getAllRepo();
     *     ResponseVO modifyRepo(ModifyRepoVO modifyRepoVO);
     */

    @Test
    public void Test1registerRepo(){
        RegisterRepoVO registerRepoVO = new RegisterRepoVO();
        registerRepoVO.setDescription("xxxxxxx");
        registerRepoVO.setGitUrl("https://gitee.com/dromara/hutool.git");
        Assert.assertEquals(REGISTER_SUCCESS, manageService.registerRepo(registerRepoVO).getContent());
        Assert.assertEquals(REPO_EXISTS, manageService.registerRepo(registerRepoVO).getMessage());
    }

    @Test
    public void Test2deleteRepo(){
        DeleteRepoVO deleteRepoVO = new DeleteRepoVO();
        deleteRepoVO.setRepoId(4L);
        Assert.assertEquals(DELETE_SUCCESS, manageService.deleteRepo(deleteRepoVO).getContent());
        Assert.assertEquals(REPO_NO_EXISTS, manageService.deleteRepo(deleteRepoVO).getMessage());
    }

    @Test
    public void Test3getAllRepo(){
        Assert.assertEquals(3, manageService.getAllRepo().size());
    }

    @Test
    public void Test4modifyRepo(){
        ModifyRepoVO modifyRepoVO = new ModifyRepoVO();
        modifyRepoVO.setRepoId(2L);
        modifyRepoVO.setDescription("TestTest");
        Assert.assertEquals(MODIFY_SUCCESS, manageService.modifyRepo(modifyRepoVO).getContent());
        modifyRepoVO.setRepoId(5L);
        Assert.assertEquals(REPO_NO_EXISTS, manageService.modifyRepo(modifyRepoVO).getMessage());
    }


}
