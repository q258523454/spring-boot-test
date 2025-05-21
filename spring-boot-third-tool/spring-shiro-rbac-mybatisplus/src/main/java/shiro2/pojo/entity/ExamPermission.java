package shiro2.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 权限表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("exam_permission")
public class ExamPermission implements Serializable {
    /**
     * 自定id,主要供前端展示权限列表分类排序使用
     */
    private Integer id;

    /**
     * 归属菜单,前端判断并展示菜单使用
     */
    private String menuCode;

    /**
     * 菜单的中文释义
     */
    private String menuName;

    /**
     * 权限的代码/通配符,对应代码中@RequiresPermissions 的value
     */
    private String permissionCode;

    /**
     * 本权限的中文释义
     */
    private String permissionName;

    /**
     * 是否本菜单必选权限, 1.必选 0.非必选 通常是"列表"权限是必选
     */
    private Boolean requiredPermission;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}