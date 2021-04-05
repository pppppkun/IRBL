package pgd.irbl.business.Service;

import pgd.irbl.business.PO.Repository;
import pgd.irbl.business.VO.DeleteRepoVO;
import pgd.irbl.business.VO.ModifyRepoVO;
import pgd.irbl.business.VO.RegisterRepoVO;
import pgd.irbl.business.VO.ResponseVO;

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
