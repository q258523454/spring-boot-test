import base.BaseJunit;
import com.annotation.util.AsyncLogUtil;
import com.annotation.util.SpringContextHolder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


public class Test4J extends BaseJunit {
    private static final Logger logger = LoggerFactory.getLogger(Test4J.class);

    @Test
    public void printBean() {
        String[] beanNameList = SpringContextHolder.getApplicationContext().getBeanDefinitionNames();
        Arrays.sort(beanNameList);
        for (String bean : beanNameList) {
            System.out.println(bean + " of Type :: " + SpringContextHolder.getApplicationContext().getBean(bean).getClass());
        }
        AsyncLogUtil asyncLogUtil = SpringContextHolder.getBean("asyncLogUtil");
        asyncLogUtil.recordLog();
    }

}
