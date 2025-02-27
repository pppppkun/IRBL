package pgd.irbl.business.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * @author qin
 * @description 消息消费者测试
 * @date 2021-06-02
 */
public class ConsumerTest {

    private String str;

    @Test
    public void consume() throws MQClientException, InterruptedException {
        //创建一个消息消费者，并设置一个消息消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_consumer_group");
        //指定 NameServer 地址
        consumer.setNamesrvAddr("124.70.133.140:9876");
        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //订阅指定 Topic 下的所有消息
        consumer.subscribe("topic-test-a", "TagTestA");
        consumer.setMessageModel(MessageModel.BROADCASTING);// 广播消费

        //注册消息监听器
        consumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext context) -> {
            //默认 list 里只有一条消息，可以通过设置参数来批量接收消息
            if (list != null) {
                for (MessageExt ext : list) {
                    try {
                        str = new String(ext.getBody(), "UTF-8");
                        System.out.println(new Date() + new String(ext.getBody(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

        });

        // 消费者对象在使用之前必须要调用 start 初始化
        consumer.start();
        System.out.println("消息消费者已启动");
//        consumer.
        Thread.sleep(3000);
        Assert.assertTrue(str.contains("Hello Java test RocketMQ"));
    }

}
