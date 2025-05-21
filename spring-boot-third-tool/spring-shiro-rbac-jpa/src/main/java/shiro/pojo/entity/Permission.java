package shiro.pojo.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * <p>
 * 权限许可（Permission) 操作 及其能访问url 权限对应一个url地址
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "system_shiro_permission")
public class Permission extends BaseEntity {
    @Column(unique = true)
    private String name;                //权限名 唯一

    @Column(unique = true)
    private String url;                 //访问地址信息 唯一

    private String description;         //描述信息

}
