package pgd.irbl.business.grpcClient;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import pgd.irbl.business.serviceImpl.protobuf.FileScore;
import pgd.irbl.business.serviceImpl.protobuf.PreProcessReply;
import pgd.irbl.business.serviceImpl.protobuf.PreProcessRequest;
import pgd.irbl.business.serviceImpl.protobuf.PreProcessorGrpc;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author qin
 * @description gRPC 调用 Java 客户端
 * @date 2021-04-14
 */
public class PreProcessorClient {
    private static final Logger logger = Logger.getLogger(PreProcessorClient.class.getName());

    private final PreProcessorGrpc.PreProcessorBlockingStub blockingStub;
    private final PreProcessorGrpc.PreProcessorFutureStub futureStub;

    public PreProcessorClient(Channel channel){
        blockingStub = PreProcessorGrpc.newBlockingStub(channel);
        futureStub = PreProcessorGrpc.newFutureStub(channel);
    }

    /**
     *  Send preprocess request to server.
     */
    public int preprocess(String codePath) {
        logger.info("try to preprocess project in " + codePath);
        PreProcessRequest request = PreProcessRequest.newBuilder().setFilePath(codePath).build();
        PreProcessReply response;

        try {
            response = blockingStub.withDeadlineAfter(6000, TimeUnit.MINUTES).protoPreprocess(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return -1;
        }
        logger.info(" preprocess project "+ codePath+" succeed" );
        return response.getSucceed();
    }

    public void preprocessAsync(String codePath, ManagedChannel preProcessorChannel,  List<FileScore> fileScoreList, String bugReportFullPath, String codeFullPath) {
        logger.info("Async: try to preprocess project in " + codePath);
        PreProcessRequest request = PreProcessRequest.newBuilder().setFilePath(codePath).build();
        PreProcessReply response;
        com.google.common.util.concurrent.ListenableFuture<PreProcessReply> listenableFuture = futureStub.protoPreprocess(request);
        Futures.addCallback(listenableFuture, new FutureCallback<PreProcessReply>() {
            @Override
            public void onSuccess(@NullableDecl PreProcessReply preProcessReply) {
                logger.info("Async: preprocess project " + codePath + " succeed");
//                preProcessReply.getSucceed();
                CalcClient calcClient = new CalcClient(preProcessorChannel);
                calcClient.calcForAsync(fileScoreList,bugReportFullPath, codeFullPath);
            }

            @Override
            public void onFailure(Throwable throwable) {
                logger.info("Async: preprocess project " + codePath + " failed");
                throwable.printStackTrace();
                //todo
//                recordService.setQueryRecordFail(recordId);

            }
        }, Executors.newFixedThreadPool(4));



//        fileScoreList = calcClient.calc(reportPath + bugReportFileName, codeDir);
    }
}
