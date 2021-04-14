package pgd.irbl.business.grpcClient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author qin
 * @description PreProcessorClientTest
 * @date 2021-04-14
 */
public class PreProcessorClientTest {

    @Test
    public void preprocess() {
        String targetPreProcessor = "localhost:50053";
        ManagedChannel channelPreProcessor = ManagedChannelBuilder.forTarget(targetPreProcessor)
                .usePlaintext()
                .build();
        PreProcessorClient preProcessorClient = new PreProcessorClient(channelPreProcessor);
        int res = preProcessorClient.preprocess("codePath");
        System.out.println(res);
    }
}