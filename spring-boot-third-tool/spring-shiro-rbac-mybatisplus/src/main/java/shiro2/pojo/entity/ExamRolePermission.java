package shiro2.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色-权限关联表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("exam_role_permission")
public class ExamRolePermission implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 权限id
     */
    private Integer permissionId;

    /**
     * 是否有限，0、无效，1、有效
     */
    private Integer isEnable;

    private Date createdAt;

    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}