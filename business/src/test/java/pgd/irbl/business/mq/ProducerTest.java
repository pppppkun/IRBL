package pgd.irbl.business.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author qin
 * @description 消息生产者测试
 * @date 2021-06-02
 */
public class ProducerTest {

    @Test
    public void produceAndSend() throws MQClientException, RemotingException, InterruptedException, MQBrokerException, UnsupportedEncodingException {
        //创建一个消息生产者，并设置一个消息生产者组
        DefaultMQProducer producer = new DefaultMQProducer("test_producer_group");
        // 消息发送失败重试次数
        producer.setRetryTimesWhenSendFailed(3);
        // 消息没有存储成功是否发送到另外一个broker
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);
        //指定 NameServer 地址
        producer.setNamesrvAddr("124.70.133.140:9876");

        //初始化 Producer，整个应用生命周期内只需要初始化一次
        producer.start();

        //创建一条消息对象，指定其主题、标签和消息内容
        Message msg = new Message(
                "topic-test-a" /* 消息主题名 */,
                "TagTestA" /* 消息标签 */,
                ("Hello Java test RocketMQ").getBytes(RemotingHelper.DEFAULT_CHARSET) /* 消息内容 */
        );
        msg.setWaitStoreMsgOK(true);

        //发送消息并返回结果
        SendResult sendResult = producer.send(msg);
//            assert sendResult.getSendStatus().equals(SendStatus.SEND_OK);
        Assert.assertEquals(sendResult.getSendStatus(), SendStatus.SEND_OK);


        // 一旦生产者实例不再被使用则将其关闭，包括清理资源，关闭网络连接等
        producer.shutdown();
    }

}
