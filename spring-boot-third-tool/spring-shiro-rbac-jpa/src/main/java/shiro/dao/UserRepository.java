package shiro.dao;

import shiro.pojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
