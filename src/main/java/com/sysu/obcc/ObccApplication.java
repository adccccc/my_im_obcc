package com.sysu.obcc;

import com.sysu.obcc.tcp.thread.ForwardMessageThread;
import com.sysu.obcc.tcp.thread.NettyServerThread;
import com.sysu.obcc.tcp.thread.RetransmissionThread;
import com.sysu.obcc.tcp.utils.MyThreadPoolManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.sysu.obcc.http.dao")
@SpringBootApplication
@EnableTransactionManagement

public class ObccApplication {

    public static void main(String[] args) {

        SpringApplication.run(ObccApplication.class, args);

        // 新线程启动Netty服务端
        MyThreadPoolManager.getInstance().execute(new NettyServerThread());

        // 4个线程启动转发消息服务
        for (int i = 0; i < 4; i++) {
            MyThreadPoolManager.getInstance().execute(new ForwardMessageThread());
        }
        // 新线程启动超时重传服务
        MyThreadPoolManager.getInstance().execute(new RetransmissionThread());
    }

}
