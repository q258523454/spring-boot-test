package readbody.intercept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created By
 *
 * @date :   2018-08-31
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private TestIntercept testIntercept;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(testIntercept).addPathPatterns("/**");
    }
}
