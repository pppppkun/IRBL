package pgd.irbl.business.ServiceImpl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pgd.irbl.business.Service.QueryService;
import pgd.irbl.business.Utils.MyFileUtil;
import pgd.irbl.business.VO.ResponseVO;
import pgd.irbl.business.grpcClient.CalcClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static pgd.irbl.business.Constant.ManageConstant.QUERY_FAIL;
import static pgd.irbl.business.Constant.ManageConstant.QUERY_NULL_FAIL;

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

    @Override
    public ResponseVO queryRegister(MultipartFile bugReport, String commitID) {
        //todo
        return null;
    }

    @Override
    public ResponseVO queryNotRegister(MultipartFile bugReport, MultipartFile sourceCode) {
        if(bugReport==null || sourceCode==null){
            return ResponseVO.buildFailure(QUERY_NULL_FAIL);
        }
        String bugReportFullPath, codeFullPath;
        try {
            bugReportFullPath = MyFileUtil.saveFile(reportPath, bugReport);
            codeFullPath = MyFileUtil.saveFile(codePath, sourceCode);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseVO.buildFailure(e);
        }
        if(bugReportFullPath==null || codeFullPath==null){
            return ResponseVO.buildFailure(null);
        }

        //todo set gRPC server port
        String target = "localhost:50051";
        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        List<FileScore> fileScoreList = null;
        try {
            CalcClient client = new CalcClient(channel);
            fileScoreList = client.calc(bugReportFullPath, codeFullPath);
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.

//            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            channel.shutdownNow();
        }

        if(fileScoreList!=null){
            return ResponseVO.buildSuccess(fileScoreList);
        }
        return ResponseVO.buildFailure(QUERY_FAIL);
    }
}
