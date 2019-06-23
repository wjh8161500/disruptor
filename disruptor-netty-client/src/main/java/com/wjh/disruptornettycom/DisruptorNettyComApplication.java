package com.wjh.disruptornettycom;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.wjh.disruptorapi.disruptor.MessageConsumer;
import com.wjh.disruptorapi.disruptor.RingBufferWorkerPoolFactory;
import com.wjh.disruptornettycom.disruptor.MessageConsumer4ClientImpl;
import com.wjh.disruptornettycom.disruptor.NettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisruptorNettyComApplication {

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(DisruptorNettyComApplication.class, args);


        //初始化disruptor
        MessageConsumer[] messageConsumers=new MessageConsumer[4];
        for (int i=0;i<messageConsumers.length;i++){
            messageConsumers[i]=new MessageConsumer4ClientImpl("code:clientId:" + i);
        }

        RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
                1024*1024,
                new BlockingWaitStrategy(),messageConsumers);

        //建立连接 并发送消息
        new NettyClient().sendData();
    }

}
