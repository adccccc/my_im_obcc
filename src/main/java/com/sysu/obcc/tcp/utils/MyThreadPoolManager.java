package com.sysu.obcc.tcp.utils;

/**
 * @Author: obc
 * @Date: 2019/2/27 20:38
 * @Version 1.0
 */

import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.*;

/**
 * 线程池封装
 */
public class MyThreadPoolManager<T> {

    // CPU核数
    private static final int CPU_COUNT = 8;

    // 核心线程数
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    // 最大线程数
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    // 非核心线程超时(s)
    private static final int KEEP_ALIVE = 1;

    // 单例模式
    private MyThreadPoolManager(){}

    private static volatile MyThreadPoolManager instance;

    // 双重检查
    public static MyThreadPoolManager getInstance() {
        if (instance == null) {
            synchronized (MyThreadPoolManager.class) {
                if (instance == null) {
                    instance = new MyThreadPoolManager();
                }
            }
        }
        return instance;
    }

    // 线程池对象
    private ThreadPoolExecutor executor;

    /**
     * 执行Runnable任务
     * @param runnable
     */
    public void execute(Runnable runnable) {
        if (executor == null) {
            synchronized (MyThreadPoolManager.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
                            new ArrayBlockingQueue<Runnable>(20), Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.AbortPolicy());
                }
            }
        }
        executor.execute(runnable);
    }

    /**
     * 执行Callable任务
     * @param callable
     * @return
     */
    public Future<T> submit(Callable<T> callable) {
        if (executor == null) {
            synchronized (MyThreadPoolManager.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
                            new ArrayBlockingQueue<Runnable>(20), Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.AbortPolicy());
                }
            }
        }
        return executor.submit(callable);
    }


    /**
     * 从等待队列中移除任务
     * @param runnable
     */
    public void cancel(Runnable runnable) {
        if (runnable != null) {
            executor.getQueue().remove(runnable);
        }
    }


}
