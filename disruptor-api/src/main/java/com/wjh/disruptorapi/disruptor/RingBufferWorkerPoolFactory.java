package com.wjh.disruptorapi.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.wjh.disruptorapi.entity.TranslatorDataWapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class RingBufferWorkerPoolFactory {

    private RingBufferWorkerPoolFactory(){
    }
    private static class SingletonHolder{
        static final RingBufferWorkerPoolFactory instance=new RingBufferWorkerPoolFactory();
    }
    public static RingBufferWorkerPoolFactory getInstance(){
        return SingletonHolder.instance;
    }

    private static Map<String,MessageConsumer> consumers=new ConcurrentHashMap<>();

    private static Map<String,MessageProducter> producters=new ConcurrentHashMap<>();

    private RingBuffer<TranslatorDataWapper> ringBuffer;

    private SequenceBarrier sequenceBarrier;

    private WorkerPool<TranslatorDataWapper> workerPool;


    public void initAndStart(ProducerType type, int bufferSize, WaitStrategy waitStrategy, MessageConsumer[] messageConsumers){

        //1. 构建ringBuffer对象
        this.ringBuffer=RingBuffer.create(type, new EventFactory<TranslatorDataWapper>() {
            @Override
            public TranslatorDataWapper newInstance() {
                return new TranslatorDataWapper();
            }
        },bufferSize,waitStrategy);

        //2.设置序号栅栏
        this.sequenceBarrier=this.ringBuffer.newBarrier();
        //3.设置工作池
        this.workerPool=new WorkerPool<TranslatorDataWapper>(this.ringBuffer,
                this.sequenceBarrier,
                new EventExceptionHandler(),
                messageConsumers);

        //4 把所构建的消费者置入池中
        for (MessageConsumer consumer:messageConsumers){
            consumers.put(consumer.getConsumerId(),consumer);
        }
        //5 添加我们的sequences
        this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());
        this.workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }


    public MessageProducter getMessageProducter(String producterId){
        MessageProducter messageProducer=this.producters.get(producterId);
        if(messageProducer==null){
            messageProducer=new MessageProducter(producterId,this.ringBuffer);
            this.producters.put(producterId,messageProducer);
        }
        return messageProducer;
    }


    /**
     * 异常静态类
     *
     * @author Alienware
     */
    static class EventExceptionHandler implements ExceptionHandler<TranslatorDataWapper> {
        public void handleEventException(Throwable ex, long sequence, TranslatorDataWapper event) {
        }

        public void handleOnStartException(Throwable ex) {
        }

        public void handleOnShutdownException(Throwable ex) {
        }
    }


}
