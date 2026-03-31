package readbody.demo;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SuccessDemo1 {
    private static final TransmittableThreadLocal<String> userContext = new TransmittableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        userContext.set("user1");
        System.out.println("父线程设置 userContext = user1");
        // 【修改点】提交任务时用 TtlRunnable 包装
        executor.submit(TtlRunnable.get(() -> {
            String value = userContext.get();
            System.out.println("任务1读取到的 userContext：" + value);
        }));

        TimeUnit.SECONDS.sleep(1);
        userContext.set("user2");
        System.out.println("父线程修改 userContext = user2");

        // 【修改点】提交任务时用 TtlRunnable 包装
        executor.submit(TtlRunnable.get(() -> {
            String value = userContext.get();
            System.out.println("任务2读取到的 userContext：" + value);
        }));

        executor.shutdown();
    }
}
