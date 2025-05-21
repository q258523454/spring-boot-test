package shiro2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"shiro2.dao"})
public class SpringShiroDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringShiroDemoApplication.class, args);
    }

}