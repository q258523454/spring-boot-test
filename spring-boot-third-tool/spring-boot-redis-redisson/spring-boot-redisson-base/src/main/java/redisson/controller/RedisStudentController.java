package redisson.controller;

import lombok.extern.slf4j.Slf4j;
import redisson.entity.Student;
import redisson.entity.StudentListObject;
import redisson.service.RedissonJackSonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class RedisStudentController {

    @Autowired
    private RedissonJackSonService redissonService;

    @GetMapping(value = "/redisson/student/set")
    public String studentSet() {
        Student student = new Student();
        student.setId(1);
        student.setAge(19);
        student.setName("zhangsan");
        redissonService.set("redisson_student", student, 60);
        return "ok";
    }

    @GetMapping(value = "/redisson/student/get")
    public String studentGet() {
        return "" + redissonService.get("redisson_student");
    }

    @GetMapping(value = "/redisson/student/list/set")
    public String studentListSet() {
        Student student = new Student();
        student.setId(1);
        student.setAge(19);
        student.setName("zhangsan");
        StudentListObject studentListObject = new StudentListObject();
        studentListObject.setName("studentListObject");
        List<Student> list = new ArrayList<>();
        list.add(student);
        studentListObject.setStudentList(list);
        redissonService.set("redisson_studentList", studentListObject, 60);
        return "ok";
    }

    @GetMapping(value = "/redisson/student/list/get")
    public String studentListGet() {
        return "" + redissonService.get("redisson_studentList");
    }
}