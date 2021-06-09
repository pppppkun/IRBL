package pgd.irbl.business.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pgd.irbl.business.dao.RepoCommitMapper;
import pgd.irbl.business.dao.RepoMapper;
import pgd.irbl.business.po.RepoCommit;
import pgd.irbl.business.po.Repository;
import pgd.irbl.business.service.ManageService;
import pgd.irbl.business.vo.*;
import pgd.irbl.business.enums.RepoState;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;

import java.io.File;
import java.io.IOException;
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
    RepoCommitMapper repoCommitMapper;

    @Autowired
    public void setRepoMapper(RepoMapper repoMapper) {
        this.repoMapper = repoMapper;
    }

    @Autowired
    public void setRepoCommitMapper(RepoCommitMapper repoCommitMapper) {
        this.repoCommitMapper = repoCommitMapper;
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

        try {
            File localPath = File.createTempFile("getCommit", "");
            if (!localPath.delete()) {
                throw new IOException("Could not delete temporary file " + localPath);
            }
            // then clone
            try {
                Git result = Git.cloneRepository()
                        .setURI("https://github.com/pppppkun/cloudNativePractice.git")
                        .setDirectory(localPath)
                        .call();
                // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
                Iterable<RevCommit> commits = result.log().all().call();
                for (RevCommit commit : commits) {
                    System.out.println("LogCommit: " + commit.getName() + " " + commit.getShortMessage());
                }
            } catch (GitAPIException ignored) { }
            FileUtils.deleteDirectory(localPath);
        } catch (IOException ignored) { }

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

    private static final String REMOTE_URL = "https://github.com/github/testrepo.git";

    @Override
    public ResponseVO dealWebhook(WebhookVO webhookVO) {
        RepoCommit repoCommit = new RepoCommit();
        repoCommit.setGitUrl(webhookVO.getGitUrl());
        repoCommit.setCommit(webhookVO.getCommitId());
        repoCommitMapper.insertRepoCommit(repoCommit);
        return ResponseVO.buildSuccess();
    }

    private static class SimpleProgressMonitor implements ProgressMonitor {
        @Override
        public void start(int totalTasks) {
            System.out.println("Starting work on " + totalTasks + " tasks");
        }

        @Override
        public void beginTask(String title, int totalWork) {
            System.out.println("Start " + title + ": " + totalWork);
        }

        @Override
        public void update(int completed) {
            System.out.print(completed + "-");
        }

        @Override
        public void endTask() {
            System.out.println("Done");
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }
}
