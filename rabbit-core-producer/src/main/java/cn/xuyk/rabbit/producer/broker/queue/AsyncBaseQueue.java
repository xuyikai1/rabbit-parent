package cn.xuyk.rabbit.producer.broker.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @Author: Xuyk
 * @Description: 异步队列
 * @Date: 2020/6/27
 */
@Slf4j
@Component
public class AsyncBaseQueue {

    @Autowired
    private ThreadPool threadPool;

    /**
     * 初始化
     */
    private ExecutorService senderAsync = null;

    /**
     * 初始化线程池
     */
    @PostConstruct
    public void initSender(){
        senderAsync = new ThreadPoolExecutor(
                threadPool.getCoreThreadSize(),
                threadPool.getMaxThreadSize(),
                threadPool.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1000),
                // 初始化 ThreadFactory
                runnable -> {
                    Thread thread = new Thread(runnable);
                    thread.setName(threadPool.getThreadPoolName());
                    return thread;
                },
                new MyAbortPolicy());
    }

    /**
     * 自定义AbortPolicy类(即自定义饱和策略的异常处理)
     */
    private static class MyAbortPolicy implements RejectedExecutionHandler {

        MyAbortPolicy() {
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error("async sender is error rejected, runnable: {}, executor: {}", r, executor);
        }
    }

    /**
     * 线程异步执行任务
     * @param runnable
     */
    public void submit(Runnable runnable) {
        senderAsync.submit(runnable);
    }

}
