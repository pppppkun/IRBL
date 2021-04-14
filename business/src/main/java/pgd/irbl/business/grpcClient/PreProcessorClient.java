package pgd.irbl.business.grpcClient;

import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import pgd.irbl.business.serviceImpl.PreProcessReply;
import pgd.irbl.business.serviceImpl.PreProcessRequest;
import pgd.irbl.business.serviceImpl.PreProcessorGrpc;

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

    public PreProcessorClient(Channel channel){
        blockingStub = PreProcessorGrpc.newBlockingStub(channel);
    }

    /** Send preprocess request to server. */
    public int preprocess(String codePath) {
        logger.info("Will try to preprocess project in " + codePath);
        PreProcessRequest request = PreProcessRequest.newBuilder().setFilePath(codePath).build();
        PreProcessReply response;

        try {
            response = blockingStub.protoPreprocess(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return -1;
        }
        logger.info("succeed" );
        return response.getSucceed();
    }
}
