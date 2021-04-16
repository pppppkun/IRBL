package pgd.irbl.business.grpcClient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Test;
import pgd.irbl.business.serviceImpl.FileScore;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author qin
 * @description CalcClientTest
 * @date 2021-04-15
 */
public class CalcClientTest {

    @Test
    public void calc() {
        String targetPreProcessor = "localhost:50051";
        ManagedChannel channelPreProcessor = ManagedChannelBuilder.forTarget(targetPreProcessor)
                .usePlaintext()
                .build();
        CalcClient calcClient = new CalcClient(channelPreProcessor);
        List<FileScore> fileScores = calcClient.calc("C:\\Users\\10444\\Desktop\\se3\\data\\report\\filename.txt", "test");
//        List<FileScore> fileScores = calcClient.calc("C:\\Users\\10444\\Desktop\\se3\\algorithm-irbl\\data\\fuck.txt", "test");
//        if(fileScores!=null){
//            for (FileScore fileScore : fileScores) {
//                System.out.println(fileScore.getFilePath() + "   " + fileScore.getScore());
//            }
//        }

    }
}