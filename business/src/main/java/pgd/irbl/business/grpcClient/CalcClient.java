package pgd.irbl.business.grpcClient;

import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import pgd.irbl.business.serviceImpl.protobuf.CalcReply;
import pgd.irbl.business.serviceImpl.protobuf.CalcRequest;
import pgd.irbl.business.serviceImpl.protobuf.CalculatorGrpc;
import pgd.irbl.business.serviceImpl.protobuf.FileScore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author qin
 * @description gRPC 调用 Java 客户端
 * @date 2021-04-11
 */
public class CalcClient {
    private static final Logger logger = Logger.getLogger(CalcClient.class.getName());

    private final CalculatorGrpc.CalculatorBlockingStub blockingStub;

    public CalcClient(Channel channel){
        blockingStub = CalculatorGrpc.newBlockingStub(channel);
    }

    /**
     *  Send calculate request to server.
     */
    public List<FileScore> calc(String bugReportFullPath, String codeFullPath) {
        logger.info(String.format("Will try to calculate bug %s bugReportFullPath: %s %s codeFullPath %s %s",
                System.lineSeparator(),
                bugReportFullPath,
                System.lineSeparator(),
                codeFullPath,
                System.lineSeparator()
                )
        );
        CalcRequest request = CalcRequest.newBuilder().setBugReportPath(bugReportFullPath).setFilePath(codeFullPath).build();
        CalcReply response;

        try {
            response = blockingStub.withDeadlineAfter(10, TimeUnit.MINUTES).calculate(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return new ArrayList<>();
        }
        logger.info("succeed" );
        return response.getEvaluationList();
    }

    public void calcForAsync(List<FileScore> fileScoreList,String bugReportFullPath, String codeFullPath) {
        logger.info(String.format("Will try to calculate bug %s bugReportFullPath: %s %s codeFullPath %s %s",
                System.lineSeparator(),
                bugReportFullPath,
                System.lineSeparator(),
                codeFullPath,
                System.lineSeparator()
                )
        );
        CalcRequest request = CalcRequest.newBuilder().setBugReportPath(bugReportFullPath).setFilePath(codeFullPath).build();
        CalcReply response;

        try {
            response = blockingStub.withDeadlineAfter(10, TimeUnit.MINUTES).calculate(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("succeed" );
        List<FileScore> fileScores = response.getEvaluationList();
        for (FileScore fileScore :
                fileScores) {
            fileScoreList.add(fileScore);
        }
        return;
    }
}
