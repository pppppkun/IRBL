package pgd.irbl.business.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pgd.irbl.business.Dao.RepoMapper;
import pgd.irbl.business.PO.Repository;
import pgd.irbl.business.service.ManageService;
import pgd.irbl.business.VO.DeleteRepoVO;
import pgd.irbl.business.VO.ModifyRepoVO;
import pgd.irbl.business.VO.RegisterRepoVO;
import pgd.irbl.business.VO.ResponseVO;
import pgd.irbl.business.enums.RepoState;

import java.sql.Date;
import java.util.List;

import static pgd.irbl.business.constant.ManageConstant.*;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-30 15:46
 */
@Service
@Slf4j
public class ManageServiceImpl implements ManageService {


    RepoMapper repoMapper;

    @Autowired
    public void setRepoMapper(RepoMapper repoMapper) {
        this.repoMapper = repoMapper;
    }

    @Override
    public ResponseVO registerRepo(RegisterRepoVO registerRepoVO) {
        if (repoMapper.findRepoIdByGitUrl(registerRepoVO.getGitUrl()) != null)
            return ResponseVO.buildFailure(REPO_EXISTS);
        Repository repository = new Repository();
        repository.setStartTime(new Date(System.currentTimeMillis()));
        repository.setQueryNum(0);
        repository.setState(RepoState.Dev);
        repository.setDescription(registerRepoVO.getDescription());
        repository.setGitUrl(registerRepoVO.getGitUrl());
        int ret = repoMapper.insertRepo(repository);
        if (ret == 0) {
            log.error(repository.getGitUrl());
            return ResponseVO.buildFailure(REGISTER_FAIL);
        } else return ResponseVO.buildSuccess(REGISTER_SUCCESS);
    }

    @Override
    public List<Repository> getAllRepo() {
        try {
            return repoMapper.selectAllRepo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResponseVO modifyRepo(ModifyRepoVO modifyRepoVO) {
        int ret = repoMapper.updateDescription(modifyRepoVO.getRepoId(), modifyRepoVO.getDescription());
        if (ret == 0) return ResponseVO.buildFailure(REPO_NO_EXISTS);
        else return ResponseVO.buildSuccess(MODIFY_SUCCESS);
    }

    @Override
    public ResponseVO deleteRepo(DeleteRepoVO deleteRepoVO) {
        int ret = repoMapper.deleteRepo(deleteRepoVO.getRepoId());
        if (ret == 0) return ResponseVO.buildFailure(REPO_NO_EXISTS);
        else return ResponseVO.buildSuccess(DELETE_SUCCESS);
    }

    @Override
    public ResponseVO dealWebhook() {
        return null;
    }
}
