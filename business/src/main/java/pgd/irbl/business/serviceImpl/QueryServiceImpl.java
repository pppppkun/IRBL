package pgd.irbl.business.serviceImpl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.service.QueryService;
import pgd.irbl.business.service.RecordService;
import pgd.irbl.business.serviceImpl.protobuf.FileScore;
import pgd.irbl.business.utils.MyFileUtil;
import pgd.irbl.business.VO.ResponseVO;
import pgd.irbl.business.grpcClient.CalcClient;
import pgd.irbl.business.grpcClient.PreProcessorClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static pgd.irbl.business.constant.ManageConstant.*;

/**
 * @Author: qin
 * @CreateTime: 2021-04-06 20:20
 */
@Service
public class QueryServiceImpl implements QueryService {

    private static final Logger logger = Logger.getLogger(QueryServiceImpl.class.getName());
    @Autowired
    RecordService recordService;

    @Value("${file.path.code}")
    String codePath;

    @Value("${file.path.report}")
    String reportPath;

    @Value("${file.path.python-cache}")
    String pythonCachePath;

    @Value("${cpu.core}")
    static Integer cpuCoreNum;

//    @Autowired()
//    @Qualifier("applicationTaskExecutor")
//    Executor executor;
//    ExecutorService executor = Executors.newFixedThreadPool(cpuCoreNum);

    @Override
    public ResponseVO queryRegister(MultipartFile bugReport, String commitID) {
        //todo
        return null;
    }

    @Override
    public ResponseVO queryNotRegister(MultipartFile bugReport, MultipartFile sourceCode, Long userId) {
        String recordId = recordService.insertQueryRecord(userId);
        Integer resCode = recordService.setQueryRecordQuerying(recordId);
        if (bugReport == null || sourceCode == null) {
            return ResponseVO.buildFailure(QUERY_NULL_FAIL);
        }
//        ExecutorService executor = Executors.newFixedThreadPool(cpuCoreNum);
        //create new Thread and run
        logger.info(" new PreprocessAndCalc thread creat");
//        executor.execute(new PreprocessAndCalc(recordService, recordId, bugReport, sourceCode));
        // 暴力了
        new Thread(new PreprocessAndCalc(recordService, recordId, bugReport, sourceCode)).run();
        logger.info(" new PreprocessAndCalc thread submit");

        assert resCode.equals(0);
        return ResponseVO.buildSuccess(recordId);
    }

    class PreprocessAndCalc implements Runnable {
        private final Logger logger = Logger.getLogger(PreprocessAndCalc.class.getName());
        String recordId;
        MultipartFile bugReport;
        MultipartFile sourceCode;
        RecordService recordService;

        PreprocessAndCalc(RecordService recordService, String recordId, MultipartFile bugReport, MultipartFile sourceCode) {
//            WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
//            RecordService recordService=(RecordService)context.getBean("recordService");
            this.recordService = recordService;
            this.recordId = recordId;
            this.bugReport = bugReport;
            this.sourceCode = sourceCode;
        }

        @Override
        public void run() {
            String bugReportFileName = null, codeDir = null;
            try {
                bugReportFileName = MyFileUtil.saveFile(reportPath, bugReport, "bugReport" + System.currentTimeMillis());
                logger.info(bugReportFileName + "bugReport save finish");
                codeDir = MyFileUtil.unZipAndSaveDir(codePath, sourceCode);
                logger.info(codeDir + "codeDir unzip finish");
            } catch (IOException e) {
                e.printStackTrace();
                recordService.setQueryRecordFail(recordId);
            }
            if (bugReportFileName == null || codeDir == null) {
                recordService.setQueryRecordFail(recordId);
            }
            // set gRPC server port
            String targetPreProcessor = "116.85.66.200:50053";
            ManagedChannel preProcessorChannel = ManagedChannelBuilder.forTarget(targetPreProcessor)
                    .usePlaintext()
                    .build();

            String target = "116.85.66.200:50051";
            // Create a communication channel to the server, known as a Channel. Channels are thread-safe
            // and reusable. It is common to create channels at the beginning of your application and reuse
            // them until the application shuts down.
            ManagedChannel calcChannel = ManagedChannelBuilder.forTarget(target)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext()
                    .build();
            List<FileScore> fileScoreList;

            try {
                PreProcessorClient preProcessorClient = new PreProcessorClient(preProcessorChannel);
                int res = preProcessorClient.preprocess(codeDir);
                if (res != 1) {
                    recordService.setQueryRecordFail(recordId);
                }
                CalcClient calcClient = new CalcClient(calcChannel);
                fileScoreList = calcClient.calc(reportPath + bugReportFileName, codeDir);
            } finally {
                // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
                // resources the channel should be shut down when it will no longer be used. If it may be used
                // again leave it running.
                calcChannel.shutdownNow();
                preProcessorChannel.shutdownNow();
            }
            List<pgd.irbl.business.VO.FileScore> voFileScoreList = new ArrayList<>();
            for (FileScore filescore : fileScoreList) {
                pgd.irbl.business.VO.FileScore tmpVOFileScore = new pgd.irbl.business.VO.FileScore();
                tmpVOFileScore.setScore(filescore.getScore());
                tmpVOFileScore.setFilePath(filescore.getFilePath());
                voFileScoreList.add(tmpVOFileScore);
            }
            if (voFileScoreList.size() == 0) {
                recordService.setQueryRecordFail(recordId);
            }
            recordService.setQueryRecordComplete(recordId, voFileScoreList);
        }
    }

}
