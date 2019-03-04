package com.sysu.obcc.tcp.thread;

/**
 * @Author: obc
 * @Date: 2019/2/28 14:31
 * @Version 1.0
 */

import com.sysu.obcc.tcp.handler.AuthHandler;
import com.sysu.obcc.tcp.handler.ImIdleStateHandler;
import com.sysu.obcc.tcp.proto.CustomProtobufDecoder;
import com.sysu.obcc.tcp.proto.CustomProtobufEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetSocketAddress;

/**
 * Netty 服务端监听Tcp连接
 */
public class NettyServerThread extends Thread{

    // TCP监听端口
    @Value("${netty.port}")
    private final int port = 17081;

    @Override
    public void run() {
        listen();
    }

    /**
     * 开启监听Tcp连接
     */
    public void listen() {

        // 监听连接线程池(serverSocket)
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 工作线程池(Socket)
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // netty服务器引导类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)

                    .localAddress(new InetSocketAddress(port))  // 设置监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            // 在这里添加Handler
//                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());      // 通过length解包bytes
//                            ch.pipeline().addLast(new ProtobufDecoder(CcPacket.AuthPacket.getDefaultInstance()));     // bytes --> bean
//                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());      // bytes前添加length标记
//                            ch.pipeline().addLast(new ProtobufEncoder());       // bean --> bytes (outBound是由后往前走)

                            ch.pipeline().addLast("idle", new ImIdleStateHandler());    // 空闲检测
                            ch.pipeline().addLast("decoder", new CustomProtobufDecoder());  // 自定义解码器
                            ch.pipeline().addLast("encoder", new CustomProtobufEncoder());  // 自定义编码器
                            ch.pipeline().addLast(new AuthHandler());       // 连接验证
                        }
                    });
            // 绑定到端口和启动服务器
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println(NettyServerThread.class.getName() + " started and listening for connections on " +
                future.channel().localAddress());

            // 等待channel关闭 退出线程
            future.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            try {
                bossGroup.shutdownGracefully().sync();
                workGroup.shutdownGracefully().sync();
            } catch (Exception e) {

            }
        }

    }
}
