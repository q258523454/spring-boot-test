package readbody.intercept;

import readbody.filter.BodyCachingRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Configuration
public class TestIntercept implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(TestIntercept.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("--------------- 拦截器: TestIntercept preHandle() ---------------");
        BodyCachingRequestWrapper requestWrapper = (BodyCachingRequestWrapper) request;
        logger.info("拦截器 key=" + requestWrapper.getParameter("key"));
        logger.info("拦截器 token=" + requestWrapper.getHeader("token"));

        // 注意:不要使用 requestWrapper.getInputStream()
        logger.info("拦截器 requestBody filterChain [getBody]:" + IOUtils.toString(requestWrapper.getBody(), requestWrapper.getCharacterEncoding()));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }


}
