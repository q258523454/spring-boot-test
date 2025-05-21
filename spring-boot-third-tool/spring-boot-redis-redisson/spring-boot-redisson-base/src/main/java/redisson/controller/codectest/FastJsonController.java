package redisson.controller.codectest;

import lombok.extern.slf4j.Slf4j;
import redisson.entity.Student;
import redisson.entity.StudentListObject;
import redisson.service.RedissonFastJsonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
public class FastJsonController {

    private static final long EXPIRE_TIME_SECONDS = 600;

    public static final String PREFIX = "test:fastjson:studentList:";

    @Autowired
    private RedissonFastJsonService redissonFastJsonService;

    @GetMapping(value = "/redisson/fastjson/set")
    public String studentSet() {
        Student student = new Student();
        student.setId(1);
        student.setAge(19);
        student.setName("zhangsan");
        redissonFastJsonService.set("fastjson_student", student, EXPIRE_TIME_SECONDS);
        return "ok";
    }

    @GetMapping(value = "/redisson/fastjson/get")
    public String studentGet() {
        return "" + redissonFastJsonService.get("fastjson_student");
    }

    /**
     * 测试平均速度:
     * 100次插入,总耗时:6936,平均耗时:69
     * 100次插入,总耗时:7915,平均耗时:79
     * 100次插入,总耗时:6869,平均耗时:68
     * 100次插入,总耗时:6607,平均耗时:66
     * 100次插入,总耗时:7357,平均耗时:73
     * 100次插入,总耗时:6739,平均耗时:67
     */
    @GetMapping(value = "/redisson/fastjson/set/list")
    public String studentSetList() {
        // 为了测试准确, 每次插入前, 先清空旧数据
        Set<String> keys = redissonFastJsonService.getKeys(PREFIX + "*");
        redissonFastJsonService.removeKeyBatch(keys);
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
            redissonFastJsonService.set(PREFIX + uuid, studentListObject, EXPIRE_TIME_SECONDS);
            stopWatch.stop();
        }
        // log.info(stopWatch.prettyPrint());
        log.info(count + "次插入,总耗时:{},平均耗时:{}", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount());
        return stopWatch.getTotalTimeMillis() + "";
    }

    /**
     * 测试平均速度:
     * 100次查询,总耗时:4771,平均耗时:47
     * 100次查询,总耗时:4133,平均耗时:41
     * 100次查询,总耗时:4820,平均耗时:48
     * 100次查询,总耗时:4111,平均耗时:41
     * 100次查询,总耗时:3978,平均耗时:39
     * 100次查询,总耗时:4456,平均耗时:44
     */
    @GetMapping(value = "/redisson/fastjson/get/list")
    public String studentGetList() {
        // 获取所有key
        Set<String> keys = redissonFastJsonService.getKeys(PREFIX + "*");

        Object list = null;
        StopWatch stopWatch = new StopWatch();
        // 循环依次查询
        int i = 0;
        for (String key : keys) {
            stopWatch.start("" + i);
            list = redissonFastJsonService.get(key);
            stopWatch.stop();
            i++;
        }
        // log.info(stopWatch.prettyPrint());
        log.info(keys.size() + "次查询,总耗时:{},平均耗时:{}", stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount());
        return list + "";
    }
}