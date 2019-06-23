package com.wjh.disruptornettycom.disruptor;

import com.wjh.disruptorapi.disruptor.MessageProducter;
import com.wjh.disruptorapi.disruptor.RingBufferWorkerPoolFactory;
import com.wjh.disruptorapi.entity.TranslatorData;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutorGroup;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//         try {
//             TranslatorData response = (TranslatorData)msg;
//             System.err.println("Client端: id= " + response.getId()
//             + ", name= " + response.getName()
//             + ", message= " + response.getMessage());
//         } finally {
//             //一定要注意 用完了缓存 要进行释放
//             ReferenceCountUtil.release(msg);
//         }


        //使用disruptor生产
        TranslatorData response = (TranslatorData)msg;
        //System.out.println("服务端返回的数据:: "+response);
        String producerId="code:seesionId:002";
        MessageProducter messageProducter =
                RingBufferWorkerPoolFactory.getInstance().getMessageProducter(producerId);
        messageProducter.onData(response,ctx);

    }
}
