package pgd.irbl.business.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;
import java.util.regex.Pattern;

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

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    JavaMailSender javaMailSender;

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
        // ([A-Za-z0-9]+@|http(|s)\:\/\/)([A-Za-z0-9.]+)(:|/)([A-Za-z0-9\/]+)(\.git)
        String gitUrl = registerRepoVO.getGitUrl();
        String pattern = "((git|ssh|http(s)?)|(git@[\\w\\.]+))(:(//)?)([\\w\\.@\\:/\\-~]+)(\\.git)(/)?";
        boolean isMatch = Pattern.matches(pattern, gitUrl);
        if(!isMatch) return ResponseVO.buildFailure("Git地址不正确~");

        try {
            try {
                String repoName = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.lastIndexOf(".git"));
                File f = new File(REPO_DIRECTION + repoName + gitUrl.hashCode());
                log.info(REPO_DIRECTION + repoName + gitUrl.hashCode());
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
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Repository repository = new Repository();
        repository.setStartTime(new Date(System.currentTimeMillis()));
        repository.setQueryNum(0);
        repository.setState(RepoState.Dev);
        repository.setDescription(registerRepoVO.getDescription());
        repository.setGitUrl(registerRepoVO.getGitUrl());
        int ret = repoMapper.insertRepo(repository);
        log.info(repository.toString());
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
        File gitDir = new File(REPO_DIRECTION + gitUrl.hashCode()+"/.git");
        List<SimpleCommitMessageVO> commitMessageVOS = new LinkedList<>();
        try (org.eclipse.jgit.lib.Repository repository = new FileRepository(gitDir)) {
            Git git = new Git(repository);
            Iterable<RevCommit> gitCommits = git.log().all().call();
            for (RevCommit commit : gitCommits) {
                if(commits.contains(commit.getName())){
                    SimpleCommitMessageVO simpleCommitMessageVO = new SimpleCommitMessageVO();
                    simpleCommitMessageVO.setCommitId(commit.getName());
                    simpleCommitMessageVO.setMessage(commit.getShortMessage());
                    simpleCommitMessageVO.setTime(new java.util.Date(commit.getCommitTime()*1000L).toString());
                    commitMessageVOS.add(simpleCommitMessageVO);
                }
            }
            log.info("add commit into databases");
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
    public ResponseVO getFileByCommit(String filepath, String commitId) {
        log.info(filepath);
        log.info(commitId);
        String gitUrl = repoCommitMapper.findGitUrlByCommitId(commitId);
        log.info(gitUrl);
        String repoName = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.lastIndexOf(".git"));
        log.info(repoName);
        try{
            Process process = Runtime.getRuntime().exec("./getFile.sh " + REPO_DIRECTION + repoName + gitUrl.hashCode() + " " + commitId + " " + filepath);
            InputStream inputStream = process.getInputStream();
            process.waitFor();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                bufferedReader.lines().forEach(System.out::println);
            }
            process.destroy();
            Path path = Paths.get(REPO_DIRECTION + filepath);
            log.info(REPO_DIRECTION + filepath);
            String s = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            return ResponseVO.buildSuccess(s);
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseVO.buildFailure("文件不存在");
    }

    @Override
    public ResponseVO deleteRepo(DeleteRepoVO deleteRepoVO) {
        String gitUrl = repoMapper.findGitUrlByRepoId(deleteRepoVO.getRepoId());
        if(gitUrl==null) return ResponseVO.buildFailure(REPO_NO_EXISTS);
        log.info("why bug");
        log.info(gitUrl);
        log.info("why bug");
        int ret = repoMapper.deleteRepo(deleteRepoVO.getRepoId());
        if(gitUrl.lastIndexOf(".git") == -1) return ResponseVO.buildSuccess(DELETE_SUCCESS);
        try{
            String repoName = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.lastIndexOf(".git"));
            Process process = Runtime.getRuntime().exec("rm -rf " + REPO_DIRECTION + repoName + gitUrl.hashCode());
            process.waitFor();
            process.destroy();
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        int i = repoCommitMapper.deleteByGitUrl(gitUrl);
        log.info("delete " + i + " commit message");
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
        File file = new File(REPO_DIRECTION + repoName + gitUrl.hashCode() + "/.git");
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
        try {
            sendSimpleMail(webhookVO.getEmail(),"There's a webhook that's triggered! COME FROM PGD-IRBL","PGD-IRBL注意到已注册的仓库"+repoName+"发出了一个WebHook，我们已经拉取最新的commit"+"("+webhookVO.getCommitId().substring(0,8)+")到我们的仓库中，欢迎进行缺陷定位~");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseVO.buildSuccess();
    }

    /**
     * 发送简单的邮件
     * @param to
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String to, String subject, String content) {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(from);
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        message.setSentDate(new Date(System.currentTimeMillis()));
        javaMailSender.send(message);
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
