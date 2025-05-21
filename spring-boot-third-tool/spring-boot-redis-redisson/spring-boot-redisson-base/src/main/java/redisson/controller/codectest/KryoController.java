package redisson.controller.codectest;

import lombok.extern.slf4j.Slf4j;
import redisson.entity.Student;
import redisson.entity.StudentListObject;
import redisson.service.RedissonKryoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
public class KryoController {

    private static final long EXPIRE_TIME_SECONDS = 600;

    public static final String PREFIX = "test:kryo:studentList:";

    @Autowired
    private RedissonKryoService redissonKryoService;

    @GetMapping(value = "/redisson/kryo/set")
    public String studentSet() {
        Student student = new Student();
        student.setId(1);
        student.setAge(19);
        student.setName("zhangsan");
        redissonKryoService.set("kryo_student", student, EXPIRE_TIME_SECONDS);
        return "ok";
    }

    @GetMapping(value = "/redisson/kryo/get")
    public String studentGet() {
        return "" + redissonKryoService.get("kryo_student");
    }

    /**
     * 测试平均速度:
     * 序列化效率 比 JackSon、FastJson 好
     * 100次插入,总耗时:2184,平均耗时:21
     * 100次插入,总耗时:1714,平均耗时:17
     * 100次插入,总耗时:1627,平均耗时:16
     * 100次插入,总耗时:1641,平均耗时:16
     * 100次插入,总耗时:2451,平均耗时:24
     * 100次插入,总耗时:1995,平均耗时:19
     */
    @GetMapping(value = "/redisson/kryo/set/list")
    public String studentSetList() {
        // 为了测试准确, 每次插入前, 先清空旧数据
        Set<String> keys = redissonKryoService.getKeys(PREFIX + "*");
        redissonKryoService.removeKeyBatch(keys);
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
            redissonKryoService.set(PREFIX + uuid, studentListObject, EXPIRE_TIME_SECONDS);
            stopWatch.stop();
        }
        // log.info(stopWatch.prettyPrint());
        log.info(count + "次插入,总耗时:{},平均耗时:{}", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount());
        return stopWatch.getTotalTimeMillis() + "";
    }

    /**
     * 测试平均速度:
     * 反序列化效率 比 JackSon、FastJson 好
     * 100次查询,总耗时:2657,平均耗时:26
     * 100次查询,总耗时:1962,平均耗时:19
     * 100次查询,总耗时:2055,平均耗时:20
     * 100次查询,总耗时:1980,平均耗时:19
     * 100次查询,总耗时:1910,平均耗时:19
     * 100次查询,总耗时:1968,平均耗时:19
     */
    @GetMapping(value = "/redisson/kryo/get/list")
    public String studentGetList() {
        // 获取所有key
        Set<String> keys = redissonKryoService.getKeys(PREFIX + "*");

        Object list = null;
        StopWatch stopWatch = new StopWatch();
        // 循环依次查询
        int i = 0;
        for (String key : keys) {
            stopWatch.start("" + i);
            list = redissonKryoService.get(key);
            stopWatch.stop();
            i++;
        }
        // log.info(stopWatch.prettyPrint());
        log.info(keys.size() + "次查询,总耗时:{},平均耗时:{}", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount());
        return list + "";
    }
}