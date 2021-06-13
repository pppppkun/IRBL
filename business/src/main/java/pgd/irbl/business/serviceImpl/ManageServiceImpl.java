package pgd.irbl.business.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.revwalk.DepthWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pgd.irbl.business.dao.RepoCommitMapper;
import pgd.irbl.business.dao.RepoMapper;
import pgd.irbl.business.po.RepoCommit;
import pgd.irbl.business.po.Repository;
import pgd.irbl.business.service.ManageService;
import pgd.irbl.business.vo.*;
import pgd.irbl.business.enums.RepoState;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

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

    @Value("${repo_direction}")
    private String REPO_DIRECTION;

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
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            REPO_DIRECTION = "E:\\4_work_dir\\source-code\\";
        }
        if (repoMapper.findRepoIdByGitUrl(registerRepoVO.getGitUrl()) != null)
            return ResponseVO.buildFailure(REPO_EXISTS);
        Repository repository = new Repository();
        repository.setStartTime(new Date(System.currentTimeMillis()));
        repository.setQueryNum(0);
        repository.setState(RepoState.Dev);
        repository.setDescription(registerRepoVO.getDescription());
        repository.setGitUrl(registerRepoVO.getGitUrl());
        int ret = repoMapper.insertRepo(repository);
        log.info(repository.toString());
        try {
            try {
                String gitUrl = repository.getGitUrl();
                String repoName = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.lastIndexOf(".git"));
                File f = new File(REPO_DIRECTION + repoName);
                Git result = Git.cloneRepository()
                        .setURI(gitUrl)
                        .setDirectory(f)
                        .setProgressMonitor(new SimpleProgressMonitor())
                        .call();
                log.info("clone success!" + gitUrl);
                // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
                Iterable<RevCommit> commits = result.log().all().call();
                List<RepoCommit> repoCommits = new ArrayList<>();
                for (RevCommit commit : commits) {
                    RepoCommit repoCommit = new RepoCommit();
                    repoCommit.setGitUrl(gitUrl);
                    repoCommit.setCommit(commit.getName());
                    repoCommits.add(repoCommit);
                }
                log.info("begin insert commit about " + gitUrl);
                int i = repoCommitMapper.insertRepoCommitByList(repoCommits);
                if(i != repoCommits.size()) return ResponseVO.buildFailure(REGISTER_FAIL);
            } catch (GitAPIException ignored) {
            }
        } catch (IOException ignored) {
        }

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
    public ResponseVO getRepoCommit(Long repoId) {
        String gitUrl = repoMapper.findGitUrlByRepoId(repoId);
        if(gitUrl==null) return ResponseVO.buildFailure(REPO_NO_EXISTS);
        Set<String> commits = new HashSet<>(repoCommitMapper.getAllCommitIdByGitUrl(gitUrl));
        String repoName = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.lastIndexOf(".git"));
        log.info(repoName);
        File gitDir = new File(REPO_DIRECTION + repoName+"/.git");
        List<SimpleCommitMessageVO> commitMessageVOS = new LinkedList<>();
        try (org.eclipse.jgit.lib.Repository repository = new FileRepository(gitDir)) {
            Git git = new Git(repository);
            Iterable<RevCommit> gitCommits = git.log().all().call();
            for (RevCommit commit : gitCommits) {
                if(commits.contains(commit.getName())){
                    SimpleCommitMessageVO simpleCommitMessageVO = new SimpleCommitMessageVO();
                    simpleCommitMessageVO.setCommitId(commit.getName());
                    simpleCommitMessageVO.setMessage(commit.getShortMessage());
                    simpleCommitMessageVO.setTime(new Timestamp(commit.getCommitTime()).toString());
                    commitMessageVOS.add(simpleCommitMessageVO);
                    log.info("add " + commit.getName());
                }
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return ResponseVO.buildSuccess(commitMessageVOS);
    }

    @Override
    public List<SimpleRepoVo> getAllSimpleRepo(){
        try {
            return repoMapper.getAllSimpleRepo();
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
        String gitUrl = repoMapper.findGitUrlByRepoId(deleteRepoVO.getRepoId());
        if(gitUrl==null) return ResponseVO.buildFailure(REPO_NO_EXISTS);
        log.info("why bug");
        log.info(gitUrl);
        log.info("why bug");
        if(gitUrl.lastIndexOf(".git") == -1) return ResponseVO.buildSuccess(DELETE_SUCCESS);
        String repoName = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.lastIndexOf(".git"));
        int ret = repoMapper.deleteRepo(deleteRepoVO.getRepoId());
        try{
            Process process = Runtime.getRuntime().exec("rm -rf " + REPO_DIRECTION + repoName);
            process.waitFor();
            process.destroy();
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (ret == 0) return ResponseVO.buildFailure(REPO_NO_EXISTS);
        else return ResponseVO.buildSuccess(DELETE_SUCCESS);
    }

    @Override
    public ResponseVO dealWebhook(WebhookVO webhookVO) {
        RepoCommit repoCommit = new RepoCommit();
        repoCommit.setGitUrl(webhookVO.getGitUrl());
        repoCommit.setCommit(webhookVO.getCommitId());
        String gitUrl = webhookVO.getGitUrl();
        String repoName = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.lastIndexOf(".git"));
        File file = new File(REPO_DIRECTION + repoName + "/.git");
        try {
            org.eclipse.jgit.lib.Repository repository = new FileRepository(file);
            try (Git git = new Git(repository)) {
                git.pull().call();
            }
        } catch (GitAPIException | IOException ignored) { }
        log.info(repoCommit.toString());
        try{
            int i = repoCommitMapper.insertRepoCommit(repoCommit);
            log.debug(i+"");
            if(i==0) return ResponseVO.buildFailure("webhook fails");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseVO.buildSuccess();
    }

    private static class SimpleProgressMonitor implements ProgressMonitor {
        int completed = 0;
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
            this.completed += completed;
            if(this.completed > 10) {
                System.out.print("-");
                this.completed = 0;
            }
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
