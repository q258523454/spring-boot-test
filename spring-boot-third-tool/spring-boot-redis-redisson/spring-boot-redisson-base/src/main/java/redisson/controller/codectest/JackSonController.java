package redisson.controller.codectest;

import lombok.extern.slf4j.Slf4j;
import redisson.entity.Student;
import redisson.entity.StudentListObject;
import redisson.service.RedissonJackSonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
public class JackSonController {

    private static final long EXPIRE_TIME_SECONDS = 600;


    public static final String PREFIX = "test:jackson:studentList:";

    @Autowired
    private RedissonJackSonService redissonJackSonService;

    @GetMapping(value = "/redisson/jackson/set")
    public String studentSet() {
        Student student = new Student();
        student.setId(1);
        student.setAge(19);
        student.setName("zhangsan");
        redissonJackSonService.set("jackson_student", student, EXPIRE_TIME_SECONDS);
        return "ok";
    }

    @GetMapping(value = "/redisson/jackson/get")
    public String studentGet() {
        return "" + redissonJackSonService.get("jackson_student");
    }

    /**
     * 测试平均速度:55/59/64/101 ms
     */
    @GetMapping(value = "/redisson/jackson/set/list")
    public String studentSetList() {
        // 为了测试准确, 每次插入前, 先清空旧数据
        Set<String> keys = redissonJackSonService.getKeys(PREFIX + "*");
        redissonJackSonService.removeKeyBatch(keys);
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
            redissonJackSonService.set(PREFIX + uuid, studentListObject, EXPIRE_TIME_SECONDS);
            stopWatch.stop();
        }
        // log.info(stopWatch.prettyPrint());
        log.info(count + "次插入,总耗时:{},平均耗时:{}", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount());
        return stopWatch.getTotalTimeMillis() + "";
    }

    /**
     * 测试平均速度:
     * 100次查询,总耗时:10765,平均耗时:107
     * 100次查询,总耗时:11312,平均耗时:113
     * 100次查询,总耗时:12205,平均耗时:122
     * 100次查询,总耗时:14624,平均耗时:146
     * 100次查询,总耗时:17021,平均耗时:170
     * 100次查询,总耗时:15398,平均耗时:153
     */
    @GetMapping(value = "/redisson/jackson/get/list")
    public String studentGetList() {
        // 获取所有key
        Set<String> keys = redissonJackSonService.getKeys(PREFIX + "*");

        Object list = null;
        StopWatch stopWatch = new StopWatch();
        // 循环依次查询
        int i = 0;
        for (String key : keys) {
            stopWatch.start("" + i);
            list = redissonJackSonService.get(key);
            stopWatch.stop();
            i++;
        }
        // log.info(stopWatch.prettyPrint());
        log.info(keys.size() + "次查询,总耗时:{},平均耗时:{}", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount());
        return list + "";
    }
}