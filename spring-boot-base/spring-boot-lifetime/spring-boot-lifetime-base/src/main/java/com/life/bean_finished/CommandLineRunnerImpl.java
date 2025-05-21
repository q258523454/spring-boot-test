package com.life.bean_finished;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @date 2020-08-17 16:55
 * @modify
 */

@Slf4j
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    /**
     * CommandLineRunner还可以接收来自启动命令和控制台的输入信息，并针对这些参数，将应用做出相应的调整。
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner");
    }
}
