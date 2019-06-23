package com.wjh.disruptornettycom.disruptor;

import com.wjh.disruptorapi.codec.MarshallingCodeCFactory;
import com.wjh.disruptorapi.entity.TranslatorData;
import com.wjh.disruptornettycom.disruptor.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class NettyClient {


    public static final String HOST="127.0.0.1";

    public static final int PORT=8765;

    //1. 创建工作线程组: 用于实际处理业务的线程组
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    //扩展 完善 池化: ConcurrentHashMap<KEY -> String, Value -> Channel>
    private Channel channel;

    private ChannelFuture cf;


    public NettyClient() throws InterruptedException {
        connect(HOST,PORT);
    }


    public void connect(String host,int port) throws InterruptedException {

        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                //表示缓存区动态调配（自适应）
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                        sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });

        this.cf = bootstrap.connect(host, port).sync();
        System.err.println("Client connected...");

        this.channel=cf.channel();
    }


    public void sendData(){
        for (int i=0;i<10;i++){
            TranslatorData request = new TranslatorData();
            request.setId("" + i);
            request.setName("请求消息名称 " + i);
            request.setMessage("请求消息内容 " + i);
            this.channel.writeAndFlush(request);
        }
    }

    public void close() throws Exception {
        cf.channel().closeFuture().sync();
        //优雅停机
        workGroup.shutdownGracefully();
        System.err.println("Sever ShutDown...");
    }



}
