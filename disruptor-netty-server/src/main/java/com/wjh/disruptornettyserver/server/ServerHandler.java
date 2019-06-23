package com.wjh.disruptornettyserver.server;

import com.wjh.disruptorapi.disruptor.MessageProducter;
import com.wjh.disruptorapi.disruptor.RingBufferWorkerPoolFactory;
import com.wjh.disruptorapi.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//
//        TranslatorData translatorData= (TranslatorData) msg;
//        System.out.println(" 服务端收到的数据是:: "+translatorData);
//        TranslatorData translatorData1=new TranslatorData();
//        translatorData1.setId("resp:"+translatorData.getId());
//        translatorData1.setName("resp:"+translatorData.getName());
//        translatorData1.setMessage("resp:"+translatorData.getMessage());
//
//        //写出response响应信息:
//        ctx.writeAndFlush(translatorData1);

        TranslatorData translatorData= (TranslatorData) msg;
        String producerId="code:serverId:001";

        MessageProducter messageProducter = RingBufferWorkerPoolFactory.getInstance().getMessageProducter(producerId);
        //生产数据
        messageProducter.onData(translatorData,ctx);

    }
}
