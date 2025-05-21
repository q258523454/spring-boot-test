package shiro2.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("exam_role")
public class ExamRole implements Serializable {
    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 是否有效  1有效  0无效
     */
    private Integer isEnable;

    private Date createdAt;

    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}