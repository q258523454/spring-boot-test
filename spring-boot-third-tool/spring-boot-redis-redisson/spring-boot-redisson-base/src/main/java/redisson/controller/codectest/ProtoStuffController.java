package redisson.controller.codectest;

import lombok.extern.slf4j.Slf4j;
import redisson.entity.Student;
import redisson.entity.StudentListObject;
import redisson.service.RedissonProtoStuffService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
public class ProtoStuffController {

    private static final long EXPIRE_TIME_SECONDS = 600;

    public static final String PREFIX = "test:protoStuff:studentList:";

    @Autowired
    private RedissonProtoStuffService redissonProtoStuffService;

    @GetMapping(value = "/redisson/protoStuff/set")
    public String studentSet() {
        Student student = new Student();
        student.setId(1);
        student.setAge(19);
        student.setName("zhangsan");
        redissonProtoStuffService.set("protoStuff_student", student, EXPIRE_TIME_SECONDS);
        return "ok";
    }

    @GetMapping(value = "/redisson/protoStuff/get")
    public String studentGet() {
        return "" + redissonProtoStuffService.get("protoStuff_student");
    }

    /**
     * 测试平均速度:
     * 序列化效率: protoStuff > kryo > fastJson > jackSon
     * 一般综合来看： 首选 kryo/protoStuff
     * 100次插入,总耗时:1792,平均耗时:17
     * 100次插入,总耗时:1335,平均耗时:13
     * 100次插入,总耗时:1270,平均耗时:12
     * 100次插入,总耗时:1285,平均耗时:12
     * 100次插入,总耗时:1311,平均耗时:13
     * 100次插入,总耗时:1451,平均耗时:14
     */
    @GetMapping(value = "/redisson/protoStuff/set/list")
    public String studentSetList() {
        // 为了测试准确, 每次插入前, 先清空旧数据
        Set<String> keys = redissonProtoStuffService.getKeys(PREFIX + "*");
        redissonProtoStuffService.removeKeyBatch(keys);
        log.info("清空 " + PREFIX + keys.size() + "个");

        // 创建插入对象(每个对象List有n个)
        int n = 100000;
        StudentListObject studentListObject = new StudentListObject();
        studentListObject.setName("studentListObject");
        studentListObject.setStudentList(StudentUtil.getRandomStudentList(n));

        // 循环插入100次,测试效率
        int count = 100;
        StopWatch stopWatch = new StopWatch();
        for (int i = 0; i < count; i++) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
            stopWatch.start("" + i);
            redissonProtoStuffService.set(PREFIX + uuid, studentListObject, EXPIRE_TIME_SECONDS);
            stopWatch.stop();
        }
        // log.info(stopWatch.prettyPrint());
        log.info(count + "次插入,总耗时:{},平均耗时:{}", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount());
        return stopWatch.getTotalTimeMillis() + "";
    }

    /**
     * 测试平均速度:
     * 反序列化效率: kryo/protoStuff(轻微差距) > fastJson > jackSon
     * 100次插入,总耗时:1451,平均耗时:14
     * 100次查询,总耗时:3965,平均耗时:39
     * 100次查询,总耗时:4635,平均耗时:46
     * 100次查询,总耗时:3435,平均耗时:34
     * 100次查询,总耗时:2990,平均耗时:29
     * 100次查询,总耗时:2563,平均耗时:25
     * 100次查询,总耗时:2536,平均耗时:25
     */
    @GetMapping(value = "/redisson/protoStuff/get/list")
    public String studentGetList() {
        // 获取所有key
        Set<String> keys = redissonProtoStuffService.getKeys(PREFIX + "*");

        Object list = null;
        StopWatch stopWatch = new StopWatch();
        // 循环依次查询
        int i = 0;
        for (String key : keys) {
            stopWatch.start("" + i);
            list = redissonProtoStuffService.get(key);
            stopWatch.stop();
            i++;
        }
        // log.info(stopWatch.prettyPrint());
        log.info(keys.size() + "次查询,总耗时:{},平均耗时:{}", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount());
        return list + "";
    }
}