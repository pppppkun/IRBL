package pgd.irbl.business.service;

import pgd.irbl.business.po.Repository;
import pgd.irbl.business.vo.DeleteRepoVO;
import pgd.irbl.business.vo.ModifyRepoVO;
import pgd.irbl.business.vo.RegisterRepoVO;
import pgd.irbl.business.vo.ResponseVO;

import java.util.List;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-25 23:40
 */
public interface ManageService {

    ResponseVO registerRepo(RegisterRepoVO registerRepoVO);
    ResponseVO deleteRepo(DeleteRepoVO deleteRepoVO);
    List<Repository> getAllRepo();
    ResponseVO modifyRepo(ModifyRepoVO modifyRepoVO);
    ResponseVO dealWebhook();

}
