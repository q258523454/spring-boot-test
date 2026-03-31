package readbody.demo;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ErrorDemo {
    // 创建 TransmittableThreadLocal 实例
    private static final TransmittableThreadLocal<String> userContext = new TransmittableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        // 创建默认线程池
        // 核心线程数 1，确保线程复用
        ExecutorService executor = Executors.newFixedThreadPool(1);

        // 第一次：父线程设置 user1，提交任务
        userContext.set("user1");
        System.out.println("父线程设置 userContext = user1");
        executor.submit(() -> {
            String value = userContext.get();
            System.out.println("任务1读取到的 userContext：" + value); // 预期：user1（首次继承）
        });

        // 等待任务1执行完毕
        TimeUnit.SECONDS.sleep(1);

        // 第二次：父线程修改为 user2，提交第二个任务（复用线程池中的线程）
        userContext.set("user2");
        System.out.println("父线程修改 userContext = user2");
        executor.submit(() -> {
            String value = userContext.get();
            System.out.println("任务2读取到的 userContext：" + value); // 实际：user1（退化，未更新）
        });

        // 关闭线程池
        executor.shutdown();
    }
}
