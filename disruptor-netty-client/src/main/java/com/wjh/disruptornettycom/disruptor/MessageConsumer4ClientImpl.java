package com.wjh.disruptornettycom.disruptor;

import com.wjh.disruptorapi.disruptor.MessageConsumer;
import com.wjh.disruptorapi.entity.TranslatorData;
import com.wjh.disruptorapi.entity.TranslatorDataWapper;
import io.netty.util.ReferenceCountUtil;

public class MessageConsumer4ClientImpl extends MessageConsumer {
    public MessageConsumer4ClientImpl(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        //服务端返回的数据进行消费
        TranslatorData translatorData = event.getTranslatorData();

        //同时可以多个消费
        //1 订单保存消费
        //2 kafka日志消费


        try {
            System.out.println("服务端返回的数据进行消费:: "+translatorData);
        }finally {
            //释放对象
            ReferenceCountUtil.release(translatorData);
        }

    }
}
