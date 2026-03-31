package readbody.config;


import com.alibaba.ttl.threadpool.TtlExecutors;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;

@Component
@Getter
public class MyThreadPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyThreadPool.class);

    private ExecutorService executor;

    @PostConstruct
    public void initExecutor() {
        LOGGER.info("init service card executor start");

        executor = new ThreadPoolExecutor(3,
                100,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(80));
        LOGGER.info("init service card executor end");
    }

    /**
     * 正确使用方法之一:TtlExecutors
     */
    // @PostConstruct
    // public void initExecutor() {
    //     LOGGER.info("init service card executor start");
    //     ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,
    //             100,
    //             60,
    //             TimeUnit.SECONDS,
    //             new LinkedBlockingDeque<>(80));
    //     executor= TtlExecutors.getTtlExecutorService(threadPoolExecutor);
    //     LOGGER.info("init service card executor end");
    // }
}