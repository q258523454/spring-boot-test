package com.mapstruct.mapping;

import com.mapstruct.entity.StudentA;
import com.mapstruct.entity.StudentB;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @Description
 * @date 2020-03-13 16:02
 * @modify
 */

@Mapper
public interface StudentMapping {
    /**
     * 如果成员变量 类型和名字 都一样,则不用写@Mapping
     * @param studentA
     * @return StudentB
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "date", target = "formatDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "ZDbkNbr", target = "DNbr") // 这是一个特殊情况
    StudentB studentAToStudentB(StudentA studentA);

    /**
     * 下面的接口, 编译后会自定实现
     * @param studentAList
     * @return
     */
    List<StudentB> studentAToStudentB(List<StudentA> studentAList);


    /**
     * 所有的字符串转字符串都会执行这个方法, 根据输入类型和返回类型来匹配的
     * 如果需要分离出来, 可以用 @Mappe(uses = StudentMappingMethod.class)
     * @param name
     * @return
     */
    default String strToString(String name) {
        System.out.println("nameToName");
        return name + ":文舰";
    }


}
