package com.jvm.controller;

import com.alibaba.fastjson.JSON;
import com.jvm.entity.TestJVM_Char;
import com.jvm.entity.TestJVM_Int;
import com.jvm.entity.TestJVM_Long;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class ControllerJvmTest {

    public static List<TestJVM_Int> testJVMInts = new CopyOnWriteArrayList<>();
    public static List<TestJVM_Char> testJVMChars = new CopyOnWriteArrayList<>();
    public static List<TestJVM_Long> testJVMLongs = new CopyOnWriteArrayList<>();

    public static Long a = 0L;

    /**
     *  name    java理论字节    VisualVm显示字节
     *  char        2               18 (对象头[16]+char[2])
     *  int         4               20 (对象头[16]+ int[4])
     *  long        8               24 (对象头[16]+long[8])
     */

    @GetMapping("/char")
    public String chars() {
        // chart:2字节
        TestJVM_Char t2 = new TestJVM_Char();
        testJVMChars.add(t2);
        return JSON.toJSONString(testJVMChars);
    }

    @GetMapping("/int")
    public String ints() {
        // int:4字节
        TestJVM_Int t1 = new TestJVM_Int();
        testJVMInts.add(t1);
        return JSON.toJSONString(testJVMInts);
    }

    @GetMapping("/long")
    public String longs() {
        // long:8字节
        TestJVM_Long t3 = new TestJVM_Long();
        testJVMLongs.add(t3);
        return JSON.toJSONString(testJVMLongs);
    }
}
