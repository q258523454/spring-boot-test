
package com.mongobase.pojo.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * The type Vote.
 */
@Data
@Document(collection = "bak_student")
@CompoundIndexes({
        @CompoundIndex(name = "idx_sid", def = "{'sid': 1}"),
})
@AllArgsConstructor
@Builder
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long sid;

    private String name;

    private Long age;

    public static Student createStudent(long sid, String name) {
        Random random = new Random();
        int nextInt = random.nextInt(100000);
        String username = name;
        if (StringUtils.isEmpty(name)) {
            username = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 3);
        }
        return new Student(sid, username, (long) nextInt);
    }


    public static List<Student> getRandomStudentList(int num, long maxSid) {
        List<Student> studentList = new ArrayList<>();
        Random random = new Random();
        for (long i = 1; i <= num; i++) {
            int nextInt = random.nextInt(100000);
            Student student = Student.builder()
                    .sid(maxSid + i)
                    .name("random_" + nextInt)
                    .age((long) nextInt)
                    .build();

            studentList.add(student);
        }
        return studentList;
    }
}
