package readbody.demo;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SuccessDemo2 {
    // 创建 TransmittableThreadLocal 实例
    private static final TransmittableThreadLocal<String> userContext = new TransmittableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        // 【修改点】 使用 TtlExecutors 封装
        ExecutorService executor = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));
        userContext.set("user1");
        System.out.println("父线程设置 userContext = user1");
        executor.submit(() -> {
            String value = userContext.get();
            System.out.println("任务1读取到的 userContext：" + value);
        });
        TimeUnit.SECONDS.sleep(1);
        userContext.set("user2");
        System.out.println("父线程修改 userContext = user2");
        executor.submit(() -> {
            String value = userContext.get();
            System.out.println("任务2读取到的 userContext：" + value);
        });
        executor.shutdown();
    }
}
