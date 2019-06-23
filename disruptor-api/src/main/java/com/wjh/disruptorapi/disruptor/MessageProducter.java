package com.wjh.disruptorapi.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.wjh.disruptorapi.entity.TranslatorData;
import com.wjh.disruptorapi.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;

public class MessageProducter {

    private RingBuffer<TranslatorDataWapper> ringBuffer;

    private String producerId;

    public MessageProducter(String producerId,RingBuffer<TranslatorDataWapper> ringBuffer) {
        this.ringBuffer = ringBuffer;
        this.producerId=producerId;
    }

    public void onData(TranslatorData data, ChannelHandlerContext ctx){

        long sequeue = ringBuffer.next();
        try {
            TranslatorDataWapper dataWapper = ringBuffer.get(sequeue);
            dataWapper.setTranslatorData(data);
            dataWapper.setCtx(ctx);

        }finally {
            ringBuffer.publish(sequeue);
        }


    }
}
