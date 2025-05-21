package com.utils;

import com.sharding5starter.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum StudentUtil {
    ;

    public static List<Student> getRandomStudentList(int n) {
        List<Student> studentList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int nextInt = random.nextInt(100000);
            Student student = Student.builder()
                    .id(nextInt)
                    .name("random_" + nextInt)
                    .age(nextInt)
                    .build();

            studentList.add(student);
        }
        return studentList;
    }
}
