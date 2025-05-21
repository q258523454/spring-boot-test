package redisson.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created By
 *
 * @date :   2018-08-28
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    /**
     * 当对象只包含基本数据类型的时候,RADM工具可以直接展示数据. 非基本数据类型的字段, 默认不会展示.
     */
    private Integer id;

    private String name;

    private Integer age;
}