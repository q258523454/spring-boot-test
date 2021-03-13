package com.lombok;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lombok.entity.Student;
import com.lombok.entity.StudentCallSupper;

public class Test {
    public static void main(String[] args) throws JsonProcessingException {

        Student student1 = new Student();
        student1.setId(1);
        student1.setName("a");
        student1.setAge(18);

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("a");
        student2.setAge(18);

        /**
         * 默认不会比较父类的属性、也不会toString父类的属性
         */
        System.out.println("student.equals(student2)：" + student1.equals(student2));
        System.out.println(student1.toString());
        System.out.println(student2.toString());


        StudentCallSupper studentCallSupper = new StudentCallSupper();
        studentCallSupper.setId(1);
        studentCallSupper.setName("b");
        studentCallSupper.setAge(20);

        StudentCallSupper studentCallSupper2 = new StudentCallSupper();
        studentCallSupper2.setId(2);
        studentCallSupper2.setName("b");
        studentCallSupper2.setAge(20);

        System.out.println("student.equals(student2)：" + studentCallSupper.equals(studentCallSupper2));
        System.out.println(studentCallSupper.toString());
        System.out.println(studentCallSupper2.toString());
    }
}
