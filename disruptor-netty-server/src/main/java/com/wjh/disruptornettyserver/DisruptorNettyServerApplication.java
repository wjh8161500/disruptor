package com.wjh.disruptornettyserver;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.wjh.disruptorapi.disruptor.MessageConsumer;
import com.wjh.disruptorapi.disruptor.RingBufferWorkerPoolFactory;
import com.wjh.disruptornettyserver.server.MessageConsumer4ServerImpl;
import com.wjh.disruptornettyserver.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisruptorNettyServerApplication {

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(DisruptorNettyServerApplication.class, args);
        //ProducerType type, int bufferSize,
        // WaitStrategy waitStrategy, MessageConsumer[] messageConsumers

        MessageConsumer[] messageConsumers=new MessageConsumer[4];

        for (int i=0;i<messageConsumers.length;i++){
            messageConsumers[i]=new MessageConsumer4ServerImpl("code:serverId:" + i);
        }


        //启动disruptor
        RingBufferWorkerPoolFactory.getInstance().
                initAndStart(ProducerType.MULTI,
                        1024*1024,
                        new BlockingWaitStrategy(),
                        messageConsumers);
        //启动netty
        new NettyServer();
    }

}
