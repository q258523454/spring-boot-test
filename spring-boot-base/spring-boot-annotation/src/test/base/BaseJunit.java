package base;

import com.annotation.AnnotationApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: zhangj
 * @Date: 2019-09-12
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnnotationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseJunit {
    private static final Logger logger = LoggerFactory.getLogger(BaseJunit.class);

    @Test
    public void runJunitTest() {
        logger.info("Junit 启动测试");
    }
}
