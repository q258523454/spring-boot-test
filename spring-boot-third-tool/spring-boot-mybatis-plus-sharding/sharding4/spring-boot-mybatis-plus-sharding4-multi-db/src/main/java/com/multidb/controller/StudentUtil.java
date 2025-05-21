package com.multidb.controller;

import com.multidb.entity.Student;
import com.multidb.entity.StudentBase;
import com.multidb.entity.StudentMasterSlave;

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


    public static List<StudentMasterSlave> getRandomStudentMasterSlavetList(int n) {
        List<StudentMasterSlave> studentList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int nextInt = random.nextInt(100000);
            StudentMasterSlave student = StudentMasterSlave.builder()
                    .id(nextInt)
                    .name("random_" + nextInt)
                    .age(nextInt)
                    .build();

            studentList.add(student);
        }
        return studentList;
    }
    public static List<StudentBase> getRandomStudentBaseList(int n) {
        List<StudentBase> studentList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int nextInt = random.nextInt(100000);
            StudentBase student = StudentBase.builder()
                    .id(nextInt)
                    .name("random_" + nextInt)
                    .age(nextInt)
                    .build();

            studentList.add(student);
        }
        return studentList;
    }
}
