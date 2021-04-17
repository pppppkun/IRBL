package pgd.irbl.business.serviceImpl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.service.QueryService;
import pgd.irbl.business.utils.MyFileUtil;
import pgd.irbl.business.VO.ResponseVO;
import pgd.irbl.business.grpcClient.CalcClient;
import pgd.irbl.business.grpcClient.PreProcessorClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static pgd.irbl.business.constant.ManageConstant.*;

/**
 * @Author: qin
 * @CreateTime: 2021-04-06 20:20
 */
@Service
public class QueryServiceImpl implements QueryService {

    @Value("${file.path.code}")
    String codePath;

    @Value("${file.path.report}")
    String reportPath;

    @Value("${file.path.python-cache}")
    String pythonCachePath;

    @Override
    public ResponseVO queryRegister(MultipartFile bugReport, String commitID) {
        //todo
        return null;
    }

    @Override
    public ResponseVO queryNotRegister(MultipartFile bugReport, MultipartFile sourceCode) {
        if (bugReport == null || sourceCode == null) {
            return ResponseVO.buildFailure(QUERY_NULL_FAIL);
        }
        String bugReportFileName, codeDir;
        try {
            bugReportFileName = MyFileUtil.saveFile(reportPath, bugReport);
//            codeDir = MyFileUtil.saveFile(codePath, sourceCode);
            codeDir = MyFileUtil.unZipAndSaveDir(codePath, sourceCode);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseVO.buildFailure(e.getMessage());
        }
        if (bugReportFileName == null || codeDir == null) {
            return ResponseVO.buildFailure(null);
        }
//        File filePythonCacheDir = new File(pythonCachePath+codeDir);
//        boolean mkdir = filePythonCacheDir.mkdir();
//        if(!mkdir){
//            return ResponseVO.buildFailure("新建文件夹失败");
//        }
        // set gRPC server port
        String targetPreProcessor = "116.85.66.200:50053";
        ManagedChannel channelPreProcessor = ManagedChannelBuilder.forTarget(targetPreProcessor)
                .usePlaintext()
                .build();

        String target = "116.85.66.200:50051";
        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        List<FileScore> fileScoreList;

        // new dir in python_cache

        try {
            PreProcessorClient preProcessorClient = new PreProcessorClient(channelPreProcessor);
            int res = preProcessorClient.preprocess(codeDir);
            if (res != 1) {
                return ResponseVO.buildFailure(PREPROCESS_NULL_FAIL);
            }
            // just for test
//            try {
//                Thread.sleep(1000*120);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            CalcClient client = new CalcClient(channel);
            fileScoreList = client.calc(reportPath + bugReportFileName, codeDir);
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.

//            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            channel.shutdownNow();
            channelPreProcessor.shutdownNow();

        }
        List<pgd.irbl.business.VO.FileScore> voFileScoreList = new ArrayList<>();
        for (FileScore filescore:fileScoreList
             ) {
            pgd.irbl.business.VO.FileScore tmpVOFileScore = new pgd.irbl.business.VO.FileScore();
            tmpVOFileScore.setScore(filescore.getScore());
            tmpVOFileScore.setFilePath(filescore.getFilePath());
            voFileScoreList.add(tmpVOFileScore);
        }


        if (voFileScoreList != null) {
            return ResponseVO.buildSuccess(voFileScoreList);
        }
        return ResponseVO.buildFailure(QUERY_FAIL);
    }
}
