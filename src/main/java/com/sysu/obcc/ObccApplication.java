package com.sysu.obcc;

import com.sysu.obcc.tcp.thread.NettyServerThread;
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
    }

}
