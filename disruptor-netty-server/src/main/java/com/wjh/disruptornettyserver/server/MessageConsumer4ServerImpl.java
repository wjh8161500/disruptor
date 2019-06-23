package com.wjh.disruptornettyserver.server;

import com.wjh.disruptorapi.disruptor.MessageConsumer;
import com.wjh.disruptorapi.entity.TranslatorData;
import com.wjh.disruptorapi.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;


/**
 * 服务端的消费
 */
public class MessageConsumer4ServerImpl extends MessageConsumer {


    public MessageConsumer4ServerImpl(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {

        TranslatorData translatorData=event.getTranslatorData();

        ChannelHandlerContext ctx = event.getCtx();

        //可以保存到数据库
        System.out.println("server 收到的数据:: "+translatorData);

        //会送给客户端数据
        TranslatorData translatorData1=new TranslatorData();
        translatorData1.setId("resp:"+translatorData.getId());
        translatorData1.setName("resp:"+translatorData.getName());
        translatorData1.setMessage("resp:"+translatorData.getMessage());
        //写出response响应信息:
        ctx.writeAndFlush(translatorData1);


    }
}
