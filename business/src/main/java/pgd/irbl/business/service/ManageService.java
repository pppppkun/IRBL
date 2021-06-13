package pgd.irbl.business.service;

import pgd.irbl.business.po.Repository;
import pgd.irbl.business.vo.*;

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
    ResponseVO dealWebhook(WebhookVO webhookVO);
    List<SimpleRepoVo> getAllSimpleRepo();
    ResponseVO getRepoCommit(Long repoId);

}
