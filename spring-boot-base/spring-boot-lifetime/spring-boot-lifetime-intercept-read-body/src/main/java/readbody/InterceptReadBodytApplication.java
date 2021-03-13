package readbody;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan // filter、servlet都需要加上这个注解
public class InterceptReadBodytApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterceptReadBodytApplication.class, args);
    }

}
