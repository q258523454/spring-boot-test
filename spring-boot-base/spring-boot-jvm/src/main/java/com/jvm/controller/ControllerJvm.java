package com.jvm.controller;

import com.alibaba.fastjson.JSON;
import com.jvm.entity.TestJVM_Long;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-13
 */


@RestController
public class ControllerJvm {

    /**
     * 启动时设置
     * vm options:
     *      -Xms64M -Xmx64M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     *      -Xms128M -Xmx128M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * --------------------- JVM简单解析 ---------------------
     * 堆空间分配情况:
     * 年轻代[1/3]= eden[8/10]、From Survivor[1/10]、To Survivor[1/10]
     * 老年代[2/3]
     * <p>
     * 清理Eden区和Survivor区叫Minor GC；
     * 清理Old区叫Major GC；
     * 清理整个堆空间—包括年轻代和老年代叫Full GC
     * ------------------------------------------------------
     *
     * @return
     */
    @GetMapping("/heep")
    public String heep() {
        return JSON.toJSONString("");
    }

    public static void main(String[] args) {
        List<TestJVM_Long> list = new ArrayList<>();
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            list.add(new TestJVM_Long());
        }
    }
    // -Xmx64M -Xms64M -XX:+PrintGCDetails

}
