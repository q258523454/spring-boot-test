package shiro.dao;

import shiro.pojo.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Permission findByName(String name);
}
