package pgd.irbl.business.serviceImpl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.dao.RepoCommitMapper;
import pgd.irbl.business.dao.RepoMapper;
import pgd.irbl.business.service.QueryService;
import pgd.irbl.business.service.RecordService;
import pgd.irbl.business.serviceImpl.protobuf.FileScore;
import pgd.irbl.business.utils.GitUtil;
import pgd.irbl.business.utils.MyFileUtil;
import pgd.irbl.business.vo.ResponseVO;
import pgd.irbl.business.grpcClient.CalcClient;
import pgd.irbl.business.grpcClient.PreProcessorClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import static pgd.irbl.business.constant.ManageConstant.*;

/**
 * @Author: qin
 * @CreateTime: 2021-04-06 20:20
 * @ModifyTime: 2021-06-16 23:20
 */
@Service
@Slf4j
public class QueryServiceImpl implements QueryService {

    private static final Logger logger = Logger.getLogger(QueryServiceImpl.class.getName());

    @Value("${file.path.code}")
    String codePath;
    @Value("${file.path.report}")
    String reportPath;
    @Value("${file.path.python-cache}")
    String pythonCachePath;
    @Value("${cpu.core}")
    static Integer cpuCoreNum;
    @Value("${target.calculator}")
    String targetCalculator;
    @Value("${target.preProcessor}")
    String targetPreProcessor;
    @Value("${repo_direction}")
    String repoDirection;
    @Autowired
    ExecutorService executor;

    GitUtil gitUtil;
    RepoCommitMapper repoCommitMapper;
    RepoMapper repoMapper;
    RecordService recordService;

    @Autowired
    public void setRepoCommitMapper(RepoCommitMapper repoCommitMapper) {
        this.repoCommitMapper = repoCommitMapper;
    }

    @Autowired
    public void setRepoMapper(RepoMapper repoMapper) {
        this.repoMapper = repoMapper;
    }

    @Autowired
    public void setGitUtil(GitUtil gitUtil) {
        this.gitUtil = gitUtil;
    }

    @Autowired
    public void setRecordService(RecordService recordService) {
        this.recordService = recordService;
    }

    @Override
    public ResponseVO queryRegister(MultipartFile bugReport, String commitId, Long userId) {
        String gitUrl = repoCommitMapper.findGitUrlByCommitId(commitId);
        String holeCommitId = repoCommitMapper.findHoleCommitId(commitId);
        int queryNum = repoMapper.findQueryNumByGitUrl(gitUrl);
        String repoName = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.lastIndexOf(".git"));
        String recordId = recordService.insertQueryRecord(userId, gitUrl, holeCommitId, repoName + "#" + queryNum, repoName + gitUrl.hashCode());
        Integer resCode = recordService.setQueryRecordQuerying(recordId);
        repoMapper.updateQueryNum(gitUrl);
        gitUtil.copyAndReset(repoName + gitUrl.hashCode(), recordId, commitId);
        if (bugReport == null) {
            return ResponseVO.buildFailure(QUERY_NULL_FAIL);
        }
        String bugReportFileName = null;
        logger.info(bugReport.getOriginalFilename());
        try {
            bugReportFileName = MyFileUtil.saveFile(reportPath, bugReport, "bugReport" + System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("enter Exception" + e.getMessage());
            recordService.setQueryRecordFail(recordId);
        }
        if (bugReportFileName == null) {
            logger.info("enter Exception bug or dir is null");
            recordService.setQueryRecordFail(recordId);
            return ResponseVO.buildFailure(QUERY_FAIL);
        }
        logger.info(bugReportFileName + " bugReport save finish");
        //create new Thread and run
        logger.info(" new PreprocessAndCalc thread creat");
        executor.execute(new PreprocessAndCalc(recordService, recordId, bugReportFileName, recordId));
        logger.info(" new PreprocessAndCalc thread submit");
        assert resCode.equals(0);
        return ResponseVO.buildSuccess(recordId);
    }

    @Override
    public ResponseVO queryNotRegister(MultipartFile bugReport, MultipartFile sourceCode, Long userId) {
        String currentTime = String.valueOf(System.currentTimeMillis());
        String zipFileName = currentTime + ".zip";
        String recordId = recordService.insertQueryRecord(userId, null, null, sourceCode.getOriginalFilename(), currentTime);
        Integer resCode = recordService.setQueryRecordQuerying(recordId);
        if (bugReport == null) {
            return ResponseVO.buildFailure(QUERY_NULL_FAIL);
        }
        // save file
        String bugReportFileName = null;
        String codeDir = null;
        try {
            logger.info(bugReport.getOriginalFilename());
            bugReportFileName = MyFileUtil.saveFile(reportPath, bugReport, "bugReport-" + currentTime);
            logger.info(bugReportFileName + " bugReport save finish");
            codeDir = MyFileUtil.unZipAndSaveDir(codePath, sourceCode, zipFileName);
            logger.info(codeDir + " codeDir unzip finish");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("enter Exception" + e.getMessage());
            recordService.setQueryRecordFail(recordId);
        }
        if (bugReportFileName == null || codeDir == null) {
            logger.info("enter Exception bug or dir is null");
            recordService.setQueryRecordFail(recordId);
            return ResponseVO.buildFailure(QUERY_FAIL);
        }
        //create new Thread and run
        logger.info(" new PreprocessAndCalc thread creat");
        executor.execute(new PreprocessAndCalc(recordService, recordId, bugReportFileName, codeDir));
        logger.info(" new PreprocessAndCalc thread submit");
        assert resCode.equals(0);

        return ResponseVO.buildSuccess(recordId);
    }

    /**
     * description 预处理与计算 线程任务类
     */
    class PreprocessAndCalc implements Runnable {
        private final Logger logger = Logger.getLogger(PreprocessAndCalc.class.getName());
        String recordId;
        String bugReportFileName;
        String codeDir;
        RecordService recordService;

        PreprocessAndCalc(RecordService recordService, String recordId, String bugReportFileName, String codeDir) {
            this.recordService = recordService;
            this.recordId = recordId;
            this.bugReportFileName = bugReportFileName;
            this.codeDir = codeDir;
        }

        @Override
        public void run() {
            // set gRPC server port
            ManagedChannel preProcessorChannel = ManagedChannelBuilder.forTarget(targetPreProcessor)
                    .usePlaintext()
                    .build();
            // Create a communication channel to the server, known as a Channel. Channels are thread-safe
            // and reusable. It is common to create channels at the beginning of your application and reuse
            // them until the application shuts down.
            ManagedChannel calcChannel = ManagedChannelBuilder.forTarget(targetCalculator)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext()
                    .build();
            List<FileScore> fileScoreList = new ArrayList<>();

            try {
                PreProcessorClient preProcessorClient = new PreProcessorClient(preProcessorChannel);
                int res = preProcessorClient.preprocess(codeDir);
                logger.info("preprocess finish");
                if (res != 1) {
                    recordService.setQueryRecordFail(recordId);
                }
                CalcClient calcClient = new CalcClient(calcChannel);
                fileScoreList = calcClient.calc(reportPath + bugReportFileName, codeDir);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
                // resources the channel should be shut down when it will no longer be used. If it may be used
                // again leave it running.
                calcChannel.shutdownNow();
                preProcessorChannel.shutdownNow();
            }
            if (fileScoreList == null) recordService.setQueryRecordFail(recordId);
            List<pgd.irbl.business.vo.FileScore> voFileScoreList = new ArrayList<>();
            for (FileScore filescore : fileScoreList) {
                pgd.irbl.business.vo.FileScore tmpVOFileScore = new pgd.irbl.business.vo.FileScore();
                tmpVOFileScore.setScore(filescore.getScore());
                String presentedFileName = filescore.getFilePath().substring(1);
                presentedFileName = presentedFileName.substring(presentedFileName.indexOf("/"));
                tmpVOFileScore.setFilePath(presentedFileName);
                voFileScoreList.add(tmpVOFileScore);
            }
            if (voFileScoreList.isEmpty()) {
                recordService.setQueryRecordFail(recordId);
            }
            recordService.setQueryRecordComplete(recordId, voFileScoreList);
        }
    }

}