package com.wjh.disruptorapi.entity;

import io.netty.channel.ChannelHandlerContext;

public class TranslatorDataWapper {

    private TranslatorData translatorData;

    private ChannelHandlerContext ctx;


    public TranslatorData getTranslatorData() {
        return translatorData;
    }

    public void setTranslatorData(TranslatorData translatorData) {
        this.translatorData = translatorData;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
