package readbody.service;

import lombok.extern.slf4j.Slf4j;
import readbody.config.MyThreadPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MyService {

    @Autowired
    private MyThreadPool myThreadPool;

    @Autowired
    private MySender mySender;

    public void runTest() {
        List<Future<String>> futureList = new ArrayList<>();
        // 模拟3个子线程执行任务
        for (int i = 0; i < 3; i++) {
            Future<String> future = myThreadPool.getExecutor().submit(() -> {
                mySender.doRequest();
                return "";
            });
            futureList.add(future);
        }

        futureList.forEach(future -> {
            try {
                String response = future.get(1, TimeUnit.SECONDS);
            } catch (Exception ex) {
                log.error("error: {}", ex.getMessage());
            }
        });
    }
}
